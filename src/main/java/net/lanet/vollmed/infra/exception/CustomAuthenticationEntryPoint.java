package net.lanet.vollmed.infra.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (authException.getCause() instanceof TokenMissingException) {
            response.sendRedirect("/error/tokenmissing");
        } else if (authException.getCause() instanceof InvalidTokenException) {
            response.sendRedirect("/error/invalidtoken");
        } else if (authException.getCause() instanceof JWTVerificationException) {
            response.sendRedirect("/error/jwtverification");
        } else {
            response.sendRedirect("/error/authenticationaccessdenied");
        }
    }
}
