
package br.com.fiap.ecoplay.dto;

public record LoginResponse(

        String token,
        String tipo,
        UsuarioResponse usuario

) {}
