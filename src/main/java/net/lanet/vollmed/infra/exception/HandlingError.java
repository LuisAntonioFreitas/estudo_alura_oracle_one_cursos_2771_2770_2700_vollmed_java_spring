package net.lanet.vollmed.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlingError {

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handlingError404(WebRequest request) {
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "",
                "Conteúdo não encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    // Erros de Validação de Dados | @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlingError400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DataValidationErrorDtoResponse::new).toList());
    }

    private record DataValidationErrorDtoResponse(String field, String message) {
        public DataValidationErrorDtoResponse(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
    // Erros de Validação de Dados | @Valid

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handlingError400(HttpMessageNotReadableException ex, WebRequest request) {
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getLocalizedMessage(),
                "Verifique a requisição.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);

    }

    // Erros de Validação de Dados de Regras de Negócio
     @ExceptionHandler(ValidationException.class)
     public ResponseEntity handlingErrorValidation(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
     }
    // Erros de Validação de Dados de Regras de Negócio

    // Security
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity handlingErrorBadCredentials() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity handlingErrorAuthentication() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity handlingErrorAccessDenied() {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
//    }
    // Security

    @ExceptionHandler(Exception.class)
    public ResponseEntity handlingError500(Exception ex, WebRequest request) {
        Map<String, Object> map = defineCustomMessageError(
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getLocalizedMessage(),
                "Erro interno do servidor.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
    }


    private Map<String, Object> defineCustomMessageError(
            String path, Integer status, String error, String details, String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("date", dateTimeUtil.getNowFormatLocal().toString());
        if (!path.trim().isEmpty()) { map.put("path", path.replace("uri=","")); }
        if (!status.toString().trim().isEmpty()) { map.put("status", status); }
        if (!error.trim().isEmpty()) { map.put("error", error); }
        if (!details.trim().isEmpty()) {
            if (!details.trim().equalsIgnoreCase(message.trim())) {
                map.put("details", details);
            }
        }
        if (!message.trim().isEmpty()) { map.put("message", message); }
        return map;
    }

}
