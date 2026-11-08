
package br.com.fiap.ecoplay.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record EntregaRequest(

        @NotNull(message = "O ponto de descarte é obrigatório")
        Long pontoId,

        @NotNull(message = "A data prevista é obrigatória")
        @FutureOrPresent(message = "A data não pode estar no passado")
        LocalDate dataPrevista,

        @NotEmpty(message = "Informe pelo menos um material")
        List<@Valid EntregaItemRequest> itens

) {}
