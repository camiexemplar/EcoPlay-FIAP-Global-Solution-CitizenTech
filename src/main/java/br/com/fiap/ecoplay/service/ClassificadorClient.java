package br.com.fiap.ecoplay.service;

import br.com.fiap.ecoplay.dto.RespostaClassificador;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ClassificadorClient {

    private final RestClient client;

    public ClassificadorClient(
            @Value("${integration.ai.base-url:http://localhost:8000}") String baseUrl) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public RespostaClassificador classificar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Envie uma imagem não vazia.");
        }

        try {
            ByteArrayResource imagem = new ByteArrayResource(arquivo.getBytes()) {
                @Override
                public String getFilename() {
                    return arquivo.getOriginalFilename() == null
                            ? "imagem"
                            : arquivo.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> partes = new LinkedMultiValueMap<>();
            partes.add("image", new HttpEntity<>(imagem, cabecalhosDaImagem(arquivo)));

            RespostaClassificador resposta = client.post()
                    .uri("/classificar")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(partes)
                    .retrieve()
                    .body(RespostaClassificador.class);

            if (resposta == null || resposta.classe() == null || resposta.confianca() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "O serviço de IA retornou uma resposta incompleta.");
            }
            return resposta;
        } catch (HttpStatusCodeException erro) {
            if (erro.getStatusCode().value() == 400 || erro.getStatusCode().value() == 413) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "O serviço de IA não aceitou a imagem.");
            }
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "O serviço de IA está indisponível.");
        } catch (IOException | RestClientException erro) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Não foi possível classificar a imagem.");
        }
    }

    private HttpHeaders cabecalhosDaImagem(MultipartFile arquivo) {
        HttpHeaders headers = new HttpHeaders();
        if (arquivo.getContentType() != null) {
            headers.setContentType(MediaType.parseMediaType(arquivo.getContentType()));
        }
        return headers;
    }
}
