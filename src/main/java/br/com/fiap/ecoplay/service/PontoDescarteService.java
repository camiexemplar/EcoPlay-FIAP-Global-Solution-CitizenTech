package br.com.fiap.ecoplay.service;
import br.com.fiap.ecoplay.dto.PontoDescarteRequest;
import br.com.fiap.ecoplay.dto.PontoDescarteResponse;
import br.com.fiap.ecoplay.entity.Material;
import br.com.fiap.ecoplay.entity.PontoDescarte;
import br.com.fiap.ecoplay.entity.Usuario;
import br.com.fiap.ecoplay.enums.Role;
import br.com.fiap.ecoplay.repository.MaterialRepository;
import br.com.fiap.ecoplay.repository.PontoDescarteRepository;
import br.com.fiap.ecoplay.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PontoDescarteService {

    private final PontoDescarteRepository pontoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public PontoDescarteResponse cadastrar(PontoDescarteRequest request) {
        PontoDescarte ponto = new PontoDescarte();
        preencher(ponto, request);

        return converter(pontoRepository.save(ponto));
    }

    @Transactional(readOnly = true)
    public PontoDescarteResponse buscarPorId(Long id) {
        return converter(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<PontoDescarteResponse> listar(Pageable pageable) {
        return pontoRepository.findAll(pageable)
                .map(this::converter);
    }

    @Transactional
    public PontoDescarteResponse atualizar(
            Long id,
            PontoDescarteRequest request) {

        PontoDescarte ponto = buscarEntidade(id);
        preencher(ponto, request);

        return converter(pontoRepository.save(ponto));
    }

    @Transactional
    public void excluir(Long id) {
        PontoDescarte ponto = buscarEntidade(id);
        pontoRepository.delete(ponto);
    }

    private PontoDescarte buscarEntidade(Long id) {
        return pontoRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ponto de descarte não encontrado: " + id));
    }

    private void preencher(
            PontoDescarte ponto,
            PontoDescarteRequest request) {

        ponto.setNome(request.nome());
        ponto.setTipo(request.tipo());
        ponto.setEndereco(request.endereco());
        ponto.setBairro(request.bairro());
        ponto.setHorario(request.horario());

        if (request.cooperativaId() != null) {
            Usuario cooperativa = usuarioRepository
                    .findById(request.cooperativaId())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "Cooperativa não encontrada"));

            if (cooperativa.getRole() != Role.COOPERATIVA) {
                throw new IllegalArgumentException(
                        "O usuário informado não é uma cooperativa");
            }

            ponto.setCooperativa(cooperativa);
        } else {
            ponto.setCooperativa(null);
        }

        Set<Material> materiais = new HashSet<>();

        if (request.materiaisAceitosIds() != null) {
            for (Long materialId : request.materiaisAceitosIds()) {
                Material material = materialRepository
                        .findById(materialId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Material não encontrado: " + materialId));

                materiais.add(material);
            }
        }

        ponto.setMateriaisAceitos(materiais);
    }

    private PontoDescarteResponse converter(PontoDescarte ponto) {
        Set<Long> materiaisIds = ponto.getMateriaisAceitos()
                .stream()
                .map(Material::getId)
                .collect(Collectors.toSet());

        Long cooperativaId = ponto.getCooperativa() != null
                ? ponto.getCooperativa().getId()
                : null;

        return new PontoDescarteResponse(
                ponto.getId(),
                ponto.getNome(),
                ponto.getTipo(),
                ponto.getEndereco(),
                ponto.getBairro(),
                ponto.getHorario(),
                cooperativaId,
                materiaisIds
        );
    }
}
