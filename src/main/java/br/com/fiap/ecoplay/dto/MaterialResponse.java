package br.com.fiap.ecoplay.dto;

import br.com.fiap.ecoplay.enums.CategoriaMaterial;

public record MaterialResponse (

        Long id,
        String nome,
        CategoriaMaterial categoria,
        Boolean reciclavel,
        String comoPreparar,
        String classeModelo
){
}
