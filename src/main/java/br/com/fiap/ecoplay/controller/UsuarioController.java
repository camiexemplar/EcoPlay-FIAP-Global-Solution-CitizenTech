package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.CadastroUsuarioRequest;
import br.com.fiap.ecoplay.dto.UsuarioResponse;
import br.com.fiap.ecoplay.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody CadastroUsuarioRequest request) {

        UsuarioResponse usuario = usuarioService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            @PathVariable Long id) {

        UsuarioResponse usuario = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuario);
    }
}
