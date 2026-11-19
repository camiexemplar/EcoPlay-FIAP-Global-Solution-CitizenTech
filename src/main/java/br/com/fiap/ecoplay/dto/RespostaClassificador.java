package br.com.fiap.ecoplay.dto;

import java.util.Map;

public record RespostaClassificador(
        String classe,
        Double confianca,
        Map<String, Double> probabilidades
) {}
