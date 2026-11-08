/**
 * TODO (INTEGRACAO IA - .NET/PYTHON):
 * Integrar os resultados da identificacao de materiais.
 * Esta entidade armazena o material identificado, a confianca
 * da IA e a confirmacao realizada pelo usuario.
 *
 * Responsavel: equipe de integracao .NET/Python.
 */

package br.com.fiap.ecoplay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "identificacao")
public class Identificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(precision = 5, scale = 4)
    private BigDecimal confianca;

    @Column(name = "confirmada_pelo_usuario", nullable = false)
    private Boolean confirmadaPeloUsuario = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }
}
