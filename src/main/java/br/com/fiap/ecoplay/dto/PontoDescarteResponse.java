
package br.com.fiap.ecoplay.dto;

import br.com.fiap.ecoplay.enums.TipoPonto;

import java.util.Set;

public record PontoDescarteResponse(

        Long id,
        String nome,
        TipoPonto tipo,
        String endereco,
        String bairro,
        String horario,
        Long cooperativaId,
        Set<Long> materiaisAceitosIds

) {}
