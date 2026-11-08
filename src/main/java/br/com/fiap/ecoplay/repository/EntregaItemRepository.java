package br.com.fiap.ecoplay.repository;

import br.com.fiap.ecoplay.entity.EntregaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaItemRepository extends JpaRepository<EntregaItem, Long> {

    List<EntregaItem> findByEntregaId(Long entregaId);
}
