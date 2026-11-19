package br.com.fiap.ecoplay.dto;

public record OrientacaoDescarteResponse(
        Long identificacaoId,
        String classe,
        String orientacao,
        String observacao
) {}
