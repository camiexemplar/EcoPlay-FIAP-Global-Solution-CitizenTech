package br.com.fiap.ecoplay.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtServicoIntegracao {

    private final JwtEncoder jwtEncoder;

    @Value("${integration.dotnet.issuer:ecoplay-java}")
    private String issuer;

    @Value("${integration.dotnet.audience:ecoplay-services}")
    private String audience;

    public String gerarToken(String usuarioId) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(audience))
                .issuedAt(agora)
                .expiresAt(agora.plus(Duration.ofMinutes(5)))
                .subject(usuarioId)
                .claim("userId", usuarioId)
                .claim("role", "SISTEMA")
                .claim("papel", "SISTEMA")
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}
