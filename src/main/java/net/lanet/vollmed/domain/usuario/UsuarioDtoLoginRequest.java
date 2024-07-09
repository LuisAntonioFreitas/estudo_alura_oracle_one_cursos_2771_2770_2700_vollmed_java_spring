package net.lanet.vollmed.domain.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioDtoLoginRequest (
        @NotBlank(message = "Login precisa ser preenchido.")
        @JsonAlias("login")
        String login,

        @NotBlank(message = "Senha precisa ser preenchida.")
        @JsonAlias("senha")
        String senha
){
}
