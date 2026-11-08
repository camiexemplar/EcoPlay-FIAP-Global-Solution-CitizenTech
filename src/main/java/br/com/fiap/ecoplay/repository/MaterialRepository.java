
package br.com.fiap.ecoplay.repository;

import br.com.fiap.ecoplay.entity.Material;
import br.com.fiap.ecoplay.enums.CategoriaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findByCategoria(CategoriaMaterial categoria);

    Optional<Material> findByClasseModelo(String classeModelo);
}
