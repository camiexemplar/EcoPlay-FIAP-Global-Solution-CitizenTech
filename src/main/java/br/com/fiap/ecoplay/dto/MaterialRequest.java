package br.com.fiap.ecoplay.dto;

import br.com.fiap.ecoplay.enums.CategoriaMaterial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaterialRequest (

    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotNull(message = "A categoria é obrigatória")
    CategoriaMaterial categoria,

    @NotNull(message = "Informe se o material é reciclável")
    Boolean reciclavel,

    String comoPreparar,

    String classeModelo
    ){}
