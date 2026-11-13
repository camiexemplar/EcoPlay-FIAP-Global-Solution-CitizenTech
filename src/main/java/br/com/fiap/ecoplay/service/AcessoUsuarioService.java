package br.com.fiap.ecoplay.security;

import br.com.fiap.ecoplay.repository.PontoDescarteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("acessoUsuarioService")
@RequiredArgsConstructor
public class AcessoUsuarioService {

    private final PontoDescarteRepository pontoRepository;

    public boolean proprioUsuario(Long usuarioId) {
        Long idAutenticado = idAutenticado();
        return usuarioId != null && usuarioId.equals(idAutenticado);
    }

    @Transactional(readOnly = true)
    public boolean pontoDaCooperativa(Long pontoId) {
        Long usuarioId = idAutenticado();

        if (pontoId == null || usuarioId == null) {
            return false;
        }

        return pontoRepository.findById(pontoId)
                .map(ponto -> ponto.getCooperativa() != null
                        && usuarioId.equals(ponto.getCooperativa().getId()))
                .orElse(false);
    }

    private Long idAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return null;
        }

        Object claim = jwt.getClaim("userId");

        if (claim instanceof Number numero) {
            return numero.longValue();
        }

        if (claim instanceof String valor) {
            try {
                return Long.parseLong(valor);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }
}
