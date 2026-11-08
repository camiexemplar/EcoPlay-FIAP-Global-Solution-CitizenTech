
package br.com.fiap.ecoplay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IdentificacaoResponse(

        Long id,
        Long usuarioId,
        Long materialId,
        BigDecimal confianca,
        Boolean confirmadaPeloUsuario,
        LocalDateTime criadoEm

) {}
