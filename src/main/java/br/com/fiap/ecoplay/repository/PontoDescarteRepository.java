package br.com.fiap.ecoplay.repository;

import br.com.fiap.ecoplay.entity.PontoDescarte;
import br.com.fiap.ecoplay.enums.TipoPonto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoDescarteRepository extends JpaRepository<PontoDescarte, Long> {

    List<PontoDescarte> findByBairroIgnoreCase(String bairro);

    List<PontoDescarte> findByTipo(TipoPonto tipo);

    List<PontoDescarte> findByCooperativaId(Long cooperativaId);
}