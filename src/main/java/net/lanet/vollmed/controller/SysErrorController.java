package net.lanet.vollmed.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lanet.vollmed.infra.exception.InvalidTokenException;
import net.lanet.vollmed.infra.exception.TokenMissingException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(allowedHeaders = "**", origins = "**", methods = {RequestMethod.GET})
@Hidden // Swagger
@Tag(name = "Error") // Swagger
@RequestMapping(path = "${api.config.path}/error")
public class SysErrorController {
    @GetMapping(path = {"/tokenmissing"})
    public ResponseEntity<Object> errorTokenMissing() {
        throw new TokenMissingException("");
    }
    @GetMapping(path = {"/invalidtoken"})
    public ResponseEntity<Object> errorInvalidToken() {
        throw new InvalidTokenException("");
    }
    @GetMapping(path = {"/jwtverification"})
    public ResponseEntity<Object> errorJwtVerification() {
        throw new JWTVerificationException("");
    }
    @GetMapping(path = {"/authenticationaccessdenied"})
    public ResponseEntity<Object> errorAuthenticationAccessDenied() {
        throw new AccessDeniedException(""); // AuthenticationException
    }
}
