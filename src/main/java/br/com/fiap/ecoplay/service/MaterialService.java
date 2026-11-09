package br.com.fiap.ecoplay.service;
import br.com.fiap.ecoplay.dto.MaterialRequest;
import br.com.fiap.ecoplay.dto.MaterialResponse;
import br.com.fiap.ecoplay.entity.Material;
import br.com.fiap.ecoplay.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Transactional
    public MaterialResponse cadastrar(MaterialRequest request) {
        Material material = new Material();
        preencher(material, request);

        return converter(materialRepository.save(material));
    }

    @Transactional(readOnly = true)
    public MaterialResponse buscarPorId(Long id) {
        return converter(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<MaterialResponse> listar(Pageable pageable) {
        return materialRepository.findAll(pageable)
                .map(this::converter);
    }

    @Transactional
    public MaterialResponse atualizar(Long id, MaterialRequest request) {
        Material material = buscarEntidade(id);
        preencher(material, request);

        return converter(materialRepository.save(material));
    }

    @Transactional
    public void excluir(Long id) {
        Material material = buscarEntidade(id);
        materialRepository.delete(material);
    }

    private Material buscarEntidade(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Material não encontrado com o ID: " + id));
    }

    private void preencher(Material material, MaterialRequest request) {
        material.setNome(request.nome());
        material.setCategoria(request.categoria());
        material.setReciclavel(request.reciclavel());
        material.setComoPreparar(request.comoPreparar());
        material.setClasseModelo(request.classeModelo());
    }

    private MaterialResponse converter(Material material) {
        return new MaterialResponse(
                material.getId(),
                material.getNome(),
                material.getCategoria(),
                material.getReciclavel(),
                material.getComoPreparar(),
                material.getClasseModelo()
        );
    }
}
