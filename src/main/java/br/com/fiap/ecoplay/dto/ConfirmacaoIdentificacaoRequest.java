
package br.com.fiap.ecoplay.dto;

import jakarta.validation.constraints.NotNull;

public record ConfirmacaoIdentificacaoRequest(

        @NotNull(message = "A confirmação é obrigatória")
        Boolean confirmadaPeloUsuario

) {}
