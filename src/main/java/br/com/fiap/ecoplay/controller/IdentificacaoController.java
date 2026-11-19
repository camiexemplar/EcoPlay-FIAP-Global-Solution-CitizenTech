
package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.ConfirmacaoIdentificacaoRequest;
import br.com.fiap.ecoplay.dto.IdentificacaoRequest;
import br.com.fiap.ecoplay.dto.IdentificacaoResponse;
import br.com.fiap.ecoplay.dto.OrientacaoDescarteResponse;
import br.com.fiap.ecoplay.dto.ResultadoClassificacaoResponse;
import br.com.fiap.ecoplay.dto.RespostaClassificador;
import br.com.fiap.ecoplay.service.ClassificadorClient;
import br.com.fiap.ecoplay.service.IdentificacaoService;
import br.com.fiap.ecoplay.service.OrientacaoDescarteService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identificacoes")
@RequiredArgsConstructor
public class IdentificacaoController {

    private final IdentificacaoService identificacaoService;
    private final ClassificadorClient classificadorClient;
    private final OrientacaoDescarteService orientacaoDescarteService;

    @PostMapping("/classificar")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p0)")
    public ResponseEntity<ResultadoClassificacaoResponse> classificar(
            @RequestParam Long usuarioId,
            @RequestPart("image") MultipartFile image) {

        RespostaClassificador classificacao = classificadorClient.classificar(image);
        ResultadoClassificacaoResponse resultado =
                identificacaoService.registrarClassificada(usuarioId, classificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PostMapping("/{id}/orientacao")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<OrientacaoDescarteResponse> orientarDescarte(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(orientacaoDescarteService.gerar(id, usuarioId));
    }

    @PostMapping
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p0)")
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
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p1)")
    public ResponseEntity<IdentificacaoResponse> buscarPorId(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(
                identificacaoService.buscarPorId(id, usuarioId)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p0)")
    public ResponseEntity<Page<IdentificacaoResponse>> listarPorUsuario(
            @PathVariable Long usuarioId,
            Pageable pageable) {

        return ResponseEntity.ok(
                identificacaoService.listarPorUsuario(
                        usuarioId, pageable)
        );
    }

    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('CIDADAO') and @acessoUsuarioService.proprioUsuario(#p1)")
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
