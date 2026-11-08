
package br.com.fiap.ecoplay.dto;

import br.com.fiap.ecoplay.enums.MotivoRecusa;
import br.com.fiap.ecoplay.enums.StatusEntrega;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EntregaResponse(

        Long id,
        Long cidadaoId,
        Long pontoId,
        LocalDate dataPrevista,
        StatusEntrega status,
        MotivoRecusa motivoRecusa,
        String observacao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        List<EntregaItemResponse> itens

) {}
