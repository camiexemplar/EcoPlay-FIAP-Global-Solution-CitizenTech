package br.com.fiap.ecoplay.entity;

import br.com.fiap.ecoplay.enums.TipoPonto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "ponto_descarte")
@NoArgsConstructor
@Entity
public class PontoDescarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoPonto tipo;

    @Column(nullable = false, length = 250)
    private String endereco;

    @Column(nullable = false, length = 150)
    private String bairro;

    @Column(length = 150)
    private String horario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperativa_id")
    private Usuario cooperativa;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ponto_material",
            joinColumns = @JoinColumn(name = "ponto_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<Material> materiaisAceitos = new HashSet<>();
}
