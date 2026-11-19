package br.com.fiap.ecoplay.dto;

import java.math.BigDecimal;

public record CreditoGamificacaoRequest(
        String usuarioId,
        String origem,
        String referencia,
        BigDecimal pesoKg,
        boolean residuoEspecial
) {}
