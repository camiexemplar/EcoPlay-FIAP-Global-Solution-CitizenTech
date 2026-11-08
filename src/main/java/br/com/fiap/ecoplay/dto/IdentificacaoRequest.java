
package br.com.fiap.ecoplay.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record IdentificacaoRequest(

        @NotNull(message = "O material é obrigatório")
        Long materialId,

        @DecimalMin(value = "0.0", message = "A confiança não pode ser negativa")
        @DecimalMax(value = "1.0", message = "A confiança não pode ser maior que 1")
        BigDecimal confianca

) {}
