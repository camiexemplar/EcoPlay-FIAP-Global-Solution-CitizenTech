
package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.EntregaRequest;
import br.com.fiap.ecoplay.dto.EntregaResponse;
import br.com.fiap.ecoplay.enums.MotivoRecusa;
import br.com.fiap.ecoplay.service.EntregaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;

    @PostMapping
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p0)")
    public ResponseEntity<EntregaResponse> agendar(
            @RequestParam Long cidadaoId,
            @Valid @RequestBody EntregaRequest request) {

        EntregaResponse entrega = entregaService.agendar(
                cidadaoId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entrega);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<EntregaResponse> buscarPorId(
            @PathVariable Long id,
            @RequestParam Long cidadaoId) {

        return ResponseEntity.ok(
                entregaService.buscarPorId(id, cidadaoId));
    }

    @GetMapping("/cidadao/{cidadaoId}")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p0)")
    public ResponseEntity<Page<EntregaResponse>> listarPorCidadao(
            @PathVariable Long cidadaoId,
            Pageable pageable) {

        return ResponseEntity.ok(
                entregaService.listarPorCidadao(
                        cidadaoId, pageable));
    }

    @GetMapping("/cooperativa/{cooperativaId}")
    @PreAuthorize("hasRole('COOPERATIVA') and @acessoUsuarioService.proprioUsuario(#p0)")
    public ResponseEntity<Page<EntregaResponse>> listarPorCooperativa(
            @PathVariable Long cooperativaId,
            Pageable pageable) {

        return ResponseEntity.ok(
                entregaService.listarPorCooperativa(
                        cooperativaId, pageable));
    }

    @PutMapping("/{id}/reagendar")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<EntregaResponse> reagendar(
            @PathVariable Long id,
            @RequestParam Long cidadaoId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate novaData) {

        return ResponseEntity.ok(
                entregaService.reagendar(
                        id, cidadaoId, novaData));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<Void> excluirAgendamento(
            @PathVariable Long id,
            @RequestParam Long cidadaoId) {

        entregaService.excluirAgendamento(id, cidadaoId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('COOPERATIVA') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<EntregaResponse> confirmar(
            @PathVariable Long id,
            @RequestParam Long cooperativaId) {

        return ResponseEntity.ok(
                entregaService.confirmar(id, cooperativaId));
    }

    @PatchMapping("/{id}/recusar")
    @PreAuthorize("hasRole('COOPERATIVA') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<EntregaResponse> recusar(
            @PathVariable Long id,
            @RequestParam Long cooperativaId,
            @RequestParam MotivoRecusa motivo,
            @RequestParam(required = false) String observacao) {

        return ResponseEntity.ok(
                entregaService.recusar(
                        id, cooperativaId, motivo, observacao));
    }

    @PatchMapping("/{id}/concluir")
    @PreAuthorize("hasRole('COOPERATIVA') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<EntregaResponse> concluir(
            @PathVariable Long id,
            @RequestParam Long cooperativaId) {

        return ResponseEntity.ok(
                entregaService.concluir(id, cooperativaId));
    }
}
