
package br.com.fiap.ecoplay.dto;

import br.com.fiap.ecoplay.enums.Role;

import java.time.LocalDateTime;

public record UsuarioResponse(

        Long id,
        String nome,
        String email,
        Role role,
        String bairro,
        LocalDateTime dataCadastro

) {}
