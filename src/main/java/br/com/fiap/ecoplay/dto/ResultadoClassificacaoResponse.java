package br.com.fiap.ecoplay.dto;

import java.util.Map;

public record ResultadoClassificacaoResponse(
        IdentificacaoResponse identificacao,
        String classe,
        Map<String, Double> probabilidades
) {}
