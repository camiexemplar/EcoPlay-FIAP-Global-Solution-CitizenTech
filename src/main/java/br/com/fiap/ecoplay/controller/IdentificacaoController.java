
package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.ConfirmacaoIdentificacaoRequest;
import br.com.fiap.ecoplay.dto.IdentificacaoRequest;
import br.com.fiap.ecoplay.dto.IdentificacaoResponse;
import br.com.fiap.ecoplay.service.IdentificacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identificacoes")
@RequiredArgsConstructor
public class IdentificacaoController {

    private final IdentificacaoService identificacaoService;

    @PostMapping
    public ResponseEntity<IdentificacaoResponse> registrar(
            @RequestParam Long usuarioId,
            @Valid @RequestBody IdentificacaoRequest request) {

        IdentificacaoResponse identificacao =
                identificacaoService.registrar(usuarioId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(identificacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentificacaoResponse> buscarPorId(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(
                identificacaoService.buscarPorId(id, usuarioId)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<IdentificacaoResponse>> listarPorUsuario(
            @PathVariable Long usuarioId,
            Pageable pageable) {

        return ResponseEntity.ok(
                identificacaoService.listarPorUsuario(
                        usuarioId, pageable)
        );
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<IdentificacaoResponse> confirmar(
            @PathVariable Long id,
            @RequestParam Long usuarioId,
            @Valid @RequestBody ConfirmacaoIdentificacaoRequest request) {

        return ResponseEntity.ok(
                identificacaoService.confirmar(
                        id, usuarioId, request)
        );
    }
}
