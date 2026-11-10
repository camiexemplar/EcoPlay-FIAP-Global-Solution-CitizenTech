
package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.LoginRequest;
import br.com.fiap.ecoplay.dto.LoginResponse;
import br.com.fiap.ecoplay.security.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                autenticacaoService.login(request)
        );
    }
}
