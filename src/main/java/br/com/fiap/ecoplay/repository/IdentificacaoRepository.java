/**
 * TODO (INTEGRACAO IA - .NET/PYTHON):
 * Utilizar este repository para persistir e consultar
 * as identificacoes recebidas dos servicos externos.
 *
 * A integracao sera implementada posteriormente.
 */

package br.com.fiap.ecoplay.repository;

import br.com.fiap.ecoplay.entity.Identificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificacaoRepository extends JpaRepository<Identificacao, Long> {

    Page<Identificacao> findByUsuarioId(Long usuarioId, Pageable pageable);
}
