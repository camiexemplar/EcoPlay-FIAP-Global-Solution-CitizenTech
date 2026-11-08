package br.com.fiap.ecoplay.repository;

import br.com.fiap.ecoplay.entity.Entrega;
import br.com.fiap.ecoplay.enums.StatusEntrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntregaRepository  extends JpaRepository<Entrega, Long> {

    Page<Entrega> findByCidadaoId(Long cidadaoId, Pageable pageable);

    Page<Entrega> findByPontoCooperativaId(Long cooperativaId, Pageable pageable);

    Page<Entrega> findByStatus(StatusEntrega status, Pageable pageable);

}
