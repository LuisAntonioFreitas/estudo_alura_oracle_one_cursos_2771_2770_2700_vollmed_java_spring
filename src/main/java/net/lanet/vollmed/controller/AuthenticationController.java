package net.lanet.vollmed.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.lanet.vollmed.domain.authentication.AuthenticationDtoLoginRequest;
import net.lanet.vollmed.domain.authentication.AuthenticationDtoView;
import net.lanet.vollmed.domain.usuario.Usuario;
import net.lanet.vollmed.infra.security.SecurityFilter;
import net.lanet.vollmed.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@Tag(name = "Login") // Swagger
@RequestMapping(path = "${api.config.path}")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService service;
    @Autowired
    private SecurityFilter securityFilter;


    @PostMapping(path = {"/login"})
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDtoLoginRequest data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var authentication = manager.authenticate(authenticationToken);

        String tokenJWT = "";
        String refreshTokenJWT = "";
        String verifyClass = authentication.getPrincipal().getClass().getSimpleName();
        switch (verifyClass.trim().toLowerCase()) {
            case "usuario":
                Object id = ((Usuario) authentication.getPrincipal()).getId();
                tokenJWT = service.generateToken(id, "usuario", null);
                refreshTokenJWT = service.generateToken(id, "usuario", "refresh");
                break;
            default:
                throw new AccessDeniedException("");
        }

        return ResponseEntity.ok(new AuthenticationDtoView(tokenJWT, refreshTokenJWT));
    }

    @SecurityRequirement(name = "bearer-key") // Swagger
    @PostMapping(path = {"/refreshtoken"})
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = securityFilter.recoverToken(request, response);

        Map<String, Object> dataToken = service.validateToken(token);
        Object subject = dataToken.get("subject");
        Object id = dataToken.get("id");

        String tokenJWT = service.generateToken(id, (String) subject, null);
        String refreshTokenJWT = service.generateToken(id, (String) subject, "refresh");

        return ResponseEntity.ok(new AuthenticationDtoView(tokenJWT, refreshTokenJWT));
    }
}
