package br.com.fiap.ecoplay.service;

import br.com.fiap.ecoplay.dto.CreditoGamificacaoRequest;
import br.com.fiap.ecoplay.security.JwtServicoIntegracao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Slf4j
@Component
public class GamificacaoClient {

    private final RestClient client;
    private final JwtServicoIntegracao jwtServico;

    public GamificacaoClient(
            JwtServicoIntegracao jwtServico,
            @Value("${integration.dotnet.base-url:http://localhost:5033}") String baseUrl) {
        this.jwtServico = jwtServico;
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void creditarIdentificacao(Long usuarioId, Long identificacaoId) {
        String referencia = "identificacao-" + identificacaoId;
        creditar(
                usuarioId,
                "IDENTIFICACAO",
                referencia,
                null,
                "identificacao-" + identificacaoId);
    }

    public void creditarEntrega(Long usuarioId, Long entregaId, BigDecimal pesoKg) {
        String referencia = "entrega-" + entregaId;
        creditar(
                usuarioId,
                "ENTREGA",
                referencia,
                pesoKg,
                "entrega-" + entregaId);
    }

    private void creditar(
            Long usuarioId,
            String origem,
            String referencia,
            BigDecimal pesoKg,
            String chaveIdempotencia) {
        try {
            client.post()
                    .uri("/api/xp/creditos")
                    .header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + jwtServico.gerarToken(usuarioId.toString()))
                    .header("Idempotency-Key", chaveIdempotencia)
                    .body(new CreditoGamificacaoRequest(
                            usuarioId.toString(), origem, referencia, pesoKg, false))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException erro) {
            log.warn(
                    "Não foi possível creditar XP no serviço .NET para {} {}. "
                            + "Uma nova confirmação poderá repetir a chamada com a mesma chave idempotente.",
                    origem,
                    referencia,
                    erro);
        }
    }
}
