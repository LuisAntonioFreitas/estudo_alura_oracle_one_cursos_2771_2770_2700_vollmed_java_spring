package net.lanet.vollmed.infra.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HandlingError {

    @Autowired
    private DateTimeUtil dtu;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handlingErrorDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        String details = (ex.getMostSpecificCause() != null) ? ex.getMostSpecificCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Violação de integridade de dados.",
                details,
                List.of());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handlingError404(EntityNotFoundException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Conteúdo não encontrado.",
                details,
                List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    // Erros de Validação de Dados | @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlingError400(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validação de dados.",
                "",
                ex.getFieldErrors().stream().map(DataValidationErrorDtoResponse::new).toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    private record DataValidationErrorDtoResponse(String field, String message) {
        public DataValidationErrorDtoResponse(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
    // Erros de Validação de Dados | @Valid

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handlingError400(HttpMessageNotReadableException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Verifique a requisição.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    // Erros de Validação de Dados de Regras de Negócio
     @ExceptionHandler(ValidationException.class)
     public ResponseEntity handlingErrorValidation(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
     }
    // Erros de Validação de Dados de Regras de Negócio

    // Security
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handlingErrorBadCredentials(BadCredentialsException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Credenciais inválidas.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handlingErrorAuthentication(AuthenticationException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                !ex.getMessage().equalsIgnoreCase("Acesso negado.")
                        ? HttpStatus.UNAUTHORIZED.value() : HttpStatus.FORBIDDEN.value(),
                !ex.getMessage().equalsIgnoreCase("Acesso negado.")
                        ? HttpStatus.UNAUTHORIZED.getReasonPhrase() : HttpStatus.FORBIDDEN.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Falha na autenticação.",
                details,
                List.of());

        return ResponseEntity.status(!ex.getMessage().equalsIgnoreCase("Acesso negado.")
                ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN).body(map);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handlingErrorAccessDenied(AccessDeniedException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Acesso negado.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }
    // Security
    // Token
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity handlingError400(JWTVerificationException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Token inválido ou expirado.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity handlingErrorTokenMissing(TokenMissingException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Token não informado.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity handlingErrorInvalidToken(InvalidTokenException ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Token inválido.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
    // Token

    @ExceptionHandler(Exception.class)
    public ResponseEntity handlingError500(Exception ex, WebRequest request) {
        String details = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getLocalizedMessage();
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Erro interno do servidor.",
                details,
                List.of());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
    }


    private Map<String, Object> defineCustomMessageError(
            String path, Integer status, String error, String message, String details,
            List<DataValidationErrorDtoResponse> listValidation) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("date", dtu.getNowFormatted(DateTimeUtil.formatter));
        if (!path.trim().isEmpty()) { map.put("path", path.replace("uri=","")); }
        if (!status.toString().trim().isEmpty()) { map.put("status", status); }
        if (!error.trim().isEmpty()) { map.put("error", error); }
        if (!message.trim().isEmpty()) { map.put("message", message); }
        if (details != null) {
            if (!details.trim().isEmpty()) {
                if (!details.trim().equalsIgnoreCase(message.trim())) {
                    map.put("details", details);
                }
            }
        }
        if (!listValidation.isEmpty()) { map.put("validation", listValidation); }
        return map;
    }

}
