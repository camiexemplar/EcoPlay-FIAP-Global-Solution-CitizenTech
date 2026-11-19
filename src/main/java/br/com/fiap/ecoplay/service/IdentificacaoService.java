package br.com.fiap.ecoplay.service;
import br.com.fiap.ecoplay.dto.ConfirmacaoIdentificacaoRequest;
import br.com.fiap.ecoplay.dto.IdentificacaoRequest;
import br.com.fiap.ecoplay.dto.IdentificacaoResponse;
import br.com.fiap.ecoplay.dto.RespostaClassificador;
import br.com.fiap.ecoplay.dto.ResultadoClassificacaoResponse;
import br.com.fiap.ecoplay.entity.Identificacao;
import br.com.fiap.ecoplay.entity.Material;
import br.com.fiap.ecoplay.entity.Usuario;
import br.com.fiap.ecoplay.repository.IdentificacaoRepository;
import br.com.fiap.ecoplay.repository.MaterialRepository;
import br.com.fiap.ecoplay.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IdentificacaoService {

    private final IdentificacaoRepository identificacaoRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final GamificacaoClient gamificacaoClient;

    @Transactional
    public IdentificacaoResponse registrar(
            Long usuarioId,
            IdentificacaoRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Usuário não encontrado: " + usuarioId));

        Material material = materialRepository
                .findById(request.materialId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Material não encontrado: "
                                        + request.materialId()));

        validarConfianca(request.confianca());

        Identificacao identificacao = new Identificacao();
        identificacao.setUsuario(usuario);
        identificacao.setMaterial(material);
        identificacao.setConfianca(request.confianca());
        identificacao.setConfirmadaPeloUsuario(false);

        return converter(identificacaoRepository.save(identificacao));
    }

    @Transactional
    public ResultadoClassificacaoResponse registrarClassificada(
            Long usuarioId,
            RespostaClassificador classificacao) {
        Material material = materialRepository
                .findByClasseModeloIgnoreCase(classificacao.classe())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Não há material cadastrado para a classe de IA: "
                                + classificacao.classe()));

        BigDecimal confianca = BigDecimal.valueOf(classificacao.confianca());
        IdentificacaoResponse identificacao = registrar(
                usuarioId,
                new IdentificacaoRequest(material.getId(), confianca));

        return new ResultadoClassificacaoResponse(
                identificacao,
                classificacao.classe(),
                classificacao.probabilidades());
    }

    @Transactional(readOnly = true)
    public IdentificacaoResponse buscarPorId(
            Long id,
            Long usuarioId) {

        Identificacao identificacao = buscarEntidade(id);
        validarProprietario(identificacao, usuarioId);

        return converter(identificacao);
    }

    @Transactional(readOnly = true)
    public Page<IdentificacaoResponse> listarPorUsuario(
            Long usuarioId,
            Pageable pageable) {

        return identificacaoRepository
                .findByUsuarioId(usuarioId, pageable)
                .map(this::converter);
    }

    @Transactional
    public IdentificacaoResponse confirmar(
            Long id,
            Long usuarioId,
            ConfirmacaoIdentificacaoRequest request) {

        Identificacao identificacao = buscarEntidade(id);
        validarProprietario(identificacao, usuarioId);

        if (request.confirmadaPeloUsuario() == null) {
            throw new IllegalArgumentException(
                    "A confirmação é obrigatória");
        }

        identificacao.setConfirmadaPeloUsuario(request.confirmadaPeloUsuario());
        Identificacao salva = identificacaoRepository.save(identificacao);

        if (Boolean.TRUE.equals(request.confirmadaPeloUsuario())) {
            Long usuarioIdConfirmado = identificacao.getUsuario().getId();
            Long identificacaoId = identificacao.getId();
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            gamificacaoClient.creditarIdentificacao(
                                    usuarioIdConfirmado, identificacaoId);
                        }
                    });
        }

        return converter(salva);
    }

    private Identificacao buscarEntidade(Long id) {
        return identificacaoRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Identificação não encontrada: " + id));
    }

    private void validarProprietario(
            Identificacao identificacao,
            Long usuarioId) {

        if (!identificacao.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException(
                    "Você não pode acessar esta identificação");
        }
    }

    private void validarConfianca(BigDecimal confianca) {
        if (confianca != null &&
                (confianca.compareTo(BigDecimal.ZERO) < 0
                        || confianca.compareTo(BigDecimal.ONE) > 0)) {
            throw new IllegalArgumentException(
                    "A confiança deve estar entre 0 e 1");
        }
    }

    private IdentificacaoResponse converter(
            Identificacao identificacao) {

        return new IdentificacaoResponse(
                identificacao.getId(),
                identificacao.getUsuario().getId(),
                identificacao.getMaterial() != null
                        ? identificacao.getMaterial().getId()
                        : null,
                identificacao.getConfianca(),
                identificacao.getConfirmadaPeloUsuario(),
                identificacao.getCriadoEm()
        );
    }
}
