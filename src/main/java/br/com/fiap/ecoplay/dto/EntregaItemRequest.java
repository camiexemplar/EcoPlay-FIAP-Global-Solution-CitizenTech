
package br.com.fiap.ecoplay.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EntregaItemRequest(

        @NotNull(message = "O material é obrigatório")
        Long materialId,

        @NotNull(message = "O peso é obrigatório")
        @DecimalMin(value = "0.01", message = "O peso deve ser maior que zero")
        BigDecimal pesoKg

) {}
