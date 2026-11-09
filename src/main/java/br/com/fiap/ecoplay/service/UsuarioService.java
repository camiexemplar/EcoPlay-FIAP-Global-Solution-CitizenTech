package br.com.fiap.ecoplay.service;
import br.com.fiap.ecoplay.dto.CadastroUsuarioRequest;
import br.com.fiap.ecoplay.dto.UsuarioResponse;
import br.com.fiap.ecoplay.entity.Usuario;
import br.com.fiap.ecoplay.enums.Role;
import br.com.fiap.ecoplay.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse cadastrar(CadastroUsuarioRequest request) {

        String email = request.email().trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "E-mail já cadastrado no sistema!"
            );
        }

        if (request.role() != Role.CIDADAO) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O cadastro público permite apenas cidadãos"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(
                passwordEncoder.encode(request.senha())
        );
        usuario.setRole(Role.CIDADAO);
        usuario.setBairro(request.bairro());

        Usuario salvo = usuarioRepository.save(usuario);

        return converterParaResponse(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado com o ID: " + id
                ));

        return converterParaResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {

        String emailNormalizado = email.trim()
                .toLowerCase(Locale.ROOT);

        return usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));
    }

    private UsuarioResponse converterParaResponse(Usuario usuario) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getBairro(),
                usuario.getDataCadastro()
        );
    }
}
