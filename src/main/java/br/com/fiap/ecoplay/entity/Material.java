package br.com.fiap.ecoplay.entity;

import br.com.fiap.ecoplay.enums.CategoriaMaterial;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "material_eco")
@NoArgsConstructor
@Entity
public class Material {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaMaterial categoria;

    @Column(nullable = false, length = 100)
    private Boolean reciclavel;

    @Column(name = "como_preparar", length = 100)
    private String comoPreparar;

    @Column(name = "classe_modelo", length = 100, unique = true)
    private String classeModelo;

}
