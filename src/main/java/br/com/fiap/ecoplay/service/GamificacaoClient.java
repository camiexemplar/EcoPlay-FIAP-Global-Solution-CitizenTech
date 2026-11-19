package br.com.fiap.ecoplay.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Envia para a API .NET de gamificação os eventos que geram XP
 * (entrega concluída e identificação confirmada).
 *
 * Usa um token de serviço com papel SISTEMA, assinado com a mesma chave RSA do login.
 * Se GAMIFICACAO_URL não estiver configurada, nada é enviado.
 * Falhas são apenas registradas no log: a gamificação nunca bloqueia a operação principal.
 */
@Slf4j
@Component
public class GamificacaoClient {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final String audience;
    private final String baseUrl;
    private final RestClient restClient = RestClient.create();

    public GamificacaoClient(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.audience}") String audience,
            @Value("${app.gamificacao.url:}") String baseUrl) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.audience = audience;
        this.baseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
    }

    public void creditarEntrega(
            Long entregaId,
            Long cidadaoId,
            BigDecimal pesoKg,
            boolean residuoEspecial) {

        String referencia = "entrega-" + entregaId;
        enviar(referencia, Map.of(
                "usuarioId", String.valueOf(cidadaoId),
                "origem", "ENTREGA",
                "referencia", referencia,
                "pesoKg", pesoKg,
                "residuoEspecial", residuoEspecial));
    }

    public void creditarIdentificacao(Long identificacaoId, Long usuarioId) {
        String referencia = "identificacao-" + identificacaoId;
        enviar(referencia, Map.of(
                "usuarioId", String.valueOf(usuarioId),
                "origem", "IDENTIFICACAO",
                "referencia", referencia));
    }

    private void enviar(String chave, Map<String, Object> corpo) {
        if (baseUrl.isBlank()) {
            return;
        }
        try {
            restClient.post()
                    .uri(baseUrl + "/api/xp/creditos")
                    .header("Authorization", "Bearer " + tokenDeServico())
                    .header("Idempotency-Key", chave)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpo)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Não foi possível creditar XP ({}): {}", chave, e.getMessage());
        }
    }

    private String tokenDeServico() {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(agora)
                .expiresAt(agora.plus(Duration.ofMinutes(5)))
                .subject("ecoplay-api")
                .audience(List.of(audience))
                .claim("role", "SISTEMA")
                .claim("papel", "SISTEMA")
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
