
package br.com.fiap.ecoplay.config;

import br.com.fiap.ecoplay.dto.MaterialRequest;
import br.com.fiap.ecoplay.dto.MaterialResponse;
import br.com.fiap.ecoplay.service.MaterialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesteMaterialConfig {

    @Bean
    CommandLineRunner testarMaterial(MaterialService materialService) {
        return args -> {

            MaterialRequest request = new MaterialRequest(
                    "Papelão",
                    br.com.fiap.ecoplay.enums.CategoriaMaterial.PAPEL,
                    true,
                    "Limpar e dobrar antes do descarte",
                    "papelao"
            );

            MaterialResponse material = materialService.cadastrar(request);

            System.out.println("=================================");
            System.out.println("MATERIAL CADASTRADO COM SUCESSO!");
            System.out.println("ID: " + material.id());
            System.out.println("Nome: " + material.nome());
            System.out.println("=================================");
        };
    }
}
