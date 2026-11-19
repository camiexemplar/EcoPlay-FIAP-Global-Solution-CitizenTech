package br.com.fiap.ecoplay.repository;

import br.com.fiap.ecoplay.entity.Identificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface IdentificacaoRepository extends JpaRepository<Identificacao, Long> {

    Page<Identificacao> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = "material")
    Optional<Identificacao> findWithMaterialById(Long id);
}
