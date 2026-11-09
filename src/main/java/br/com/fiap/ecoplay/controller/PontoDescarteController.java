
package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.PontoDescarteRequest;
import br.com.fiap.ecoplay.dto.PontoDescarteResponse;
import br.com.fiap.ecoplay.service.PontoDescarteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pontos-descarte")
@RequiredArgsConstructor
public class PontoDescarteController {

    private final PontoDescarteService pontoDescarteService;

    @PostMapping
    public ResponseEntity<PontoDescarteResponse> cadastrar(
            @Valid @RequestBody PontoDescarteRequest request) {

        PontoDescarteResponse ponto =
                pontoDescarteService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ponto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoDescarteResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pontoDescarteService.buscarPorId(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<PontoDescarteResponse>> listar(
            Pageable pageable) {

        return ResponseEntity.ok(
                pontoDescarteService.listar(pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoDescarteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PontoDescarteRequest request) {

        return ResponseEntity.ok(
                pontoDescarteService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        pontoDescarteService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
