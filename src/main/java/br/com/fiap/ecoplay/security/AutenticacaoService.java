
package br.com.fiap.ecoplay.security;

import br.com.fiap.ecoplay.dto.LoginRequest;
import br.com.fiap.ecoplay.dto.LoginResponse;
import br.com.fiap.ecoplay.dto.UsuarioResponse;
import br.com.fiap.ecoplay.entity.Usuario;
import br.com.fiap.ecoplay.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(this::credenciaisInvalidas);

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenhaHash())) {

            throw credenciaisInvalidas();
        }

        String token = jwtService.gerarToken(usuario);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getBairro(),
                usuario.getDataCadastro()
        );

        return new LoginResponse(
                token,
                "Bearer",
                usuarioResponse
        );
    }

    private ResponseStatusException credenciaisInvalidas() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "E-mail ou senha inválidos"
        );
    }
}
