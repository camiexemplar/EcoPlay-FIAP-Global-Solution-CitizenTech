
package br.com.fiap.ecoplay.dto;

import br.com.fiap.ecoplay.enums.TipoPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PontoDescarteRequest(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "O tipo do ponto é obrigatório")
        TipoPonto tipo,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotBlank(message = "O bairro é obrigatório")
        String bairro,

        String horario,

        Long cooperativaId,

        Set<Long> materiaisAceitosIds

) {}
