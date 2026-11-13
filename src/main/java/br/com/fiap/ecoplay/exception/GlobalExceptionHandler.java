
package br.com.fiap.ecoplay.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.access.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> tratarAcessoNegadoSpring(
            AccessDeniedException exception,
            HttpServletRequest request) {

        return criarResposta(
                403,
                "Você não tem permissão para realizar esta operação",
                request
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> tratarErroHttp(
            ResponseStatusException exception,
            HttpServletRequest request) {

        String mensagem = exception.getReason() != null
                ? exception.getReason()
                : "Não foi possível processar a solicitação";

        return criarResposta(
                exception.getStatusCode().value(),
                mensagem,
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String mensagem = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("Dados inválidos");

        return criarResposta(
                400,
                mensagem,
                request
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(
            NoSuchElementException exception,
            HttpServletRequest request) {

        return criarResposta(
                404,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarArgumentoInvalido(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        return criarResposta(
                400,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> tratarConflitoEstado(
            IllegalStateException exception,
            HttpServletRequest request) {

        return criarResposta(
                409,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> tratarAcessoNegado(
            SecurityException exception,
            HttpServletRequest request) {

        return criarResposta(
                403,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        return criarResposta(
                400,
                "JSON inválido ou campos com valores incompatíveis",
                request
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tratarTipoInvalido(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {

        return criarResposta(
                400,
                "Valor inválido para o parâmetro: "
                        + exception.getName(),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarIntegridade(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {

        return criarResposta(
                409,
                "Operação não permitida por uma restrição de dados",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroInterno(
            Exception exception,
            HttpServletRequest request) {

        exception.printStackTrace();

        return criarResposta(
                500,
                "Ocorreu um erro interno no servidor",
                request
        );
    }

    private ResponseEntity<Map<String, Object>> criarResposta(
            int status,
            String mensagem,
            HttpServletRequest request) {

        Map<String, Object> resposta = new LinkedHashMap<>();

        HttpStatus httpStatus = HttpStatus.resolve(status);

        resposta.put("status", status);
        resposta.put(
                "erro",
                httpStatus != null
                        ? httpStatus.getReasonPhrase()
                        : "Erro HTTP"
        );
        resposta.put("mensagem", mensagem);
        resposta.put("caminho", request.getRequestURI());

        return ResponseEntity
                .status(status)
                .body(resposta);
    }
}
