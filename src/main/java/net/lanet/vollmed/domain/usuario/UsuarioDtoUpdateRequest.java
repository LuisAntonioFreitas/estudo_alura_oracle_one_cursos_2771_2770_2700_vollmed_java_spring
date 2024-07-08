package net.lanet.vollmed.domain.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.*;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioDtoUpdateRequest(
        @NotBlank(message = "Nome precisa ser preenchido.")
        @Size(max = 255, message = "Nome não pode conter mais do que 255 caracteres.")
        @JsonAlias("nome")
        String nome,

        @NotBlank(message = "Login precisa ser preenchido.")
        @Size(max = 100, message = "Login não pode conter mais do que 100 caracteres.")
        @JsonAlias("login")
        String login,

        @NotBlank(message = "Email precisa ser preenchido.")
        @Size(max = 255, message = "Email não pode conter mais do que 255 caracteres.")
        @Email(message = "Email precisa ser preenchido corretamente.")
        @JsonAlias("email")
        String email
) {
}
