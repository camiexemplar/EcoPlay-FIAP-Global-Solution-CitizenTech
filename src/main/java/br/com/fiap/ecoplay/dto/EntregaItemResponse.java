
package br.com.fiap.ecoplay.dto;

import java.math.BigDecimal;

public record EntregaItemResponse(

        Long id,
        Long materialId,
        BigDecimal pesoKg

) {}
