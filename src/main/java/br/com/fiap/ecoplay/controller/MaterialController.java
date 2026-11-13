
package br.com.fiap.ecoplay.controller;

import br.com.fiap.ecoplay.dto.MaterialRequest;
import br.com.fiap.ecoplay.dto.MaterialResponse;
import br.com.fiap.ecoplay.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materiais")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @PreAuthorize("hasRole('COOPERATIVA')")
    public ResponseEntity<MaterialResponse> cadastrar(
            @Valid @RequestBody MaterialRequest request) {

        MaterialResponse material = materialService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(material);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                materialService.buscarPorId(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<MaterialResponse>> listar(
            Pageable pageable) {

        return ResponseEntity.ok(
                materialService.listar(pageable)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COOPERATIVA')")
    public ResponseEntity<MaterialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MaterialRequest request) {

        return ResponseEntity.ok(
                materialService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COOPERATIVA')")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        materialService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
