package br.com.fiap.ecoplay.service;
import br.com.fiap.ecoplay.dto.*;
import br.com.fiap.ecoplay.entity.*;
import br.com.fiap.ecoplay.enums.MotivoRecusa;
import br.com.fiap.ecoplay.enums.Role;
import br.com.fiap.ecoplay.enums.StatusEntrega;
import br.com.fiap.ecoplay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final EntregaItemRepository itemRepository;
    private final UsuarioRepository usuarioRepository;
    private final PontoDescarteRepository pontoRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public EntregaResponse agendar(
            Long cidadaoId,
            EntregaRequest request) {

        Usuario cidadao = usuarioRepository.findById(cidadaoId)
                .orElseThrow(() ->
                        new NoSuchElementException("Cidadão não encontrado"));

        if (cidadao.getRole() != Role.CIDADAO) {
            throw new IllegalArgumentException(
                    "Somente cidadãos podem agendar entregas");
        }

        PontoDescarte ponto = pontoRepository.findById(request.pontoId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ponto de descarte não encontrado"));

        validarData(request.dataPrevista());

        if (request.itens() == null || request.itens().isEmpty()) {
            throw new IllegalArgumentException(
                    "Informe pelo menos um material");
        }

        Entrega entrega = new Entrega();
        entrega.setCidadao(cidadao);
        entrega.setPonto(ponto);
        entrega.setDataPrevista(request.dataPrevista());
        entrega.setStatus(StatusEntrega.AGENDADA);
        entrega.setCriadoEm(LocalDate.now());

        entrega = entregaRepository.save(entrega);

        for (EntregaItemRequest itemRequest : request.itens()) {
            Material material = materialRepository
                    .findById(itemRequest.materialId())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "Material não encontrado: "
                                            + itemRequest.materialId()));

            if (!ponto.getMateriaisAceitos().contains(material)
                    && ponto.getMateriaisAceitos().stream()
                    .noneMatch(m -> m.getId().equals(material.getId()))) {
                throw new IllegalArgumentException(
                        "O ponto não aceita o material: " + material.getNome());
            }

            if (itemRequest.pesoKg() == null
                    || itemRequest.pesoKg().signum() <= 0) {
                throw new IllegalArgumentException(
                        "O peso deve ser maior que zero");
            }

            EntregaItem item = new EntregaItem();
            item.setEntrega(entrega);
            item.setMaterial(material);
            item.setPesoKg(itemRequest.pesoKg());

            itemRepository.save(item);
        }

        return converter(entrega);
    }

    @Transactional(readOnly = true)
    public EntregaResponse buscarPorId(Long id, Long cidadaoId) {
        Entrega entrega = buscarEntidade(id);
        validarCidadao(entrega, cidadaoId);
        return converter(entrega);
    }

    @Transactional(readOnly = true)
    public Page<EntregaResponse> listarPorCidadao(
            Long cidadaoId,
            Pageable pageable) {

        return entregaRepository.findByCidadaoId(cidadaoId, pageable)
                .map(this::converter);
    }

    @Transactional(readOnly = true)
    public Page<EntregaResponse> listarPorCooperativa(
            Long cooperativaId,
            Pageable pageable) {

        return entregaRepository
                .findByPontoCooperativaId(cooperativaId, pageable)
                .map(this::converter);
    }

    @Transactional
    public EntregaResponse reagendar(
            Long id,
            Long cidadaoId,
            LocalDate novaData) {

        Entrega entrega = buscarEntidade(id);
        validarCidadao(entrega, cidadaoId);
        validarStatus(entrega, StatusEntrega.AGENDADA);
        validarData(novaData);

        entrega.setDataPrevista(novaData);

        return converter(entregaRepository.save(entrega));
    }

    @Transactional
    public void excluirAgendamento(Long id, Long cidadaoId) {
        Entrega entrega = buscarEntidade(id);
        validarCidadao(entrega, cidadaoId);
        validarStatus(entrega, StatusEntrega.AGENDADA);

        List<EntregaItem> itens = buscarItens(entrega);
        itemRepository.deleteAll(itens);
        itemRepository.flush();
        entregaRepository.delete(entrega);
    }

    @Transactional
    public EntregaResponse confirmar(Long id, Long cooperativaId) {
        Entrega entrega = buscarEntidade(id);
        validarCooperativa(entrega, cooperativaId);
        validarStatus(entrega, StatusEntrega.AGENDADA);

        entrega.setStatus(StatusEntrega.CONFIRMADA);

        return converter(entregaRepository.save(entrega));
    }

    @Transactional
    public EntregaResponse recusar(
            Long id,
            Long cooperativaId,
            MotivoRecusa motivo,
            String observacao) {

        Entrega entrega = buscarEntidade(id);
        validarCooperativa(entrega, cooperativaId);

        if (entrega.getStatus() != StatusEntrega.AGENDADA
                && entrega.getStatus() != StatusEntrega.CONFIRMADA) {
            throw new IllegalStateException(
                    "Esta entrega não pode ser recusada");
        }

        if (motivo == null) {
            throw new IllegalArgumentException(
                    "O motivo da recusa é obrigatório");
        }

        entrega.setStatus(StatusEntrega.RECUSADA);
        entrega.setMotivoRecusa(motivo);
        entrega.setObservacao(observacao);

        return converter(entregaRepository.save(entrega));
    }

    @Transactional
    public EntregaResponse concluir(Long id, Long cooperativaId) {
        Entrega entrega = buscarEntidade(id);
        validarCooperativa(entrega, cooperativaId);
        validarStatus(entrega, StatusEntrega.CONFIRMADA);

        entrega.setStatus(StatusEntrega.CONCLUIDA);

        return converter(entregaRepository.save(entrega));
    }

    private Entrega buscarEntidade(Long id) {
        return entregaRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Entrega não encontrada: " + id));
    }

    private void validarCidadao(Entrega entrega, Long cidadaoId) {
        if (!entrega.getCidadao().getId().equals(cidadaoId)) {
            throw new SecurityException(
                    "Você não pode acessar a entrega de outro cidadão");
        }
    }

    private void validarCooperativa(
            Entrega entrega,
            Long cooperativaId) {

        Usuario cooperativa = entrega.getPonto().getCooperativa();

        if (cooperativa == null
                || !cooperativa.getId().equals(cooperativaId)
                || cooperativa.getRole() != Role.COOPERATIVA) {
            throw new SecurityException(
                    "Esta entrega não pertence à sua cooperativa");
        }
    }

    private void validarStatus(
            Entrega entrega,
            StatusEntrega esperado) {

        if (entrega.getStatus() != esperado) {
            throw new IllegalStateException(
                    "Operação não permitida para o status atual");
        }
    }

    private void validarData(LocalDate data) {
        if (data == null || data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "A data não pode estar no passado");
        }
    }

    private List<EntregaItem> buscarItens(Entrega entrega) {
        return itemRepository.findByEntregaId(entrega.getId());
    }

    private EntregaResponse converter(Entrega entrega) {
        List<EntregaItemResponse> itens = buscarItens(entrega)
                .stream()
                .map(item -> new EntregaItemResponse(
                        item.getId(),
                        item.getMaterial().getId(),
                        item.getPesoKg()
                ))
                .toList();

        return new EntregaResponse(
                entrega.getId(),
                entrega.getCidadao().getId(),
                entrega.getPonto().getId(),
                entrega.getDataPrevista(),
                entrega.getStatus(),
                entrega.getMotivoRecusa(),
                entrega.getObservacao(),
                entrega.getCriadoEm() != null
                        ? entrega.getCriadoEm().atStartOfDay()
                        : null,
                null,
                itens
        );
    }
}
