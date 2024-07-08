package net.lanet.vollmed.domain.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioDtoSenhaRequest(
        @NotBlank(message = "Senha precisa ser preenchida.")
        @JsonAlias("senha")
        String senha,

        @NotBlank(message = "Nova Senha precisa ser preenchida.")
        @JsonAlias("novasenha")
        String novaSenha,

        @NotBlank(message = "Confirma Nova Senha precisa ser preenchida.")
        @JsonAlias("confirmanovasenha")
        String confirmaNovaSenha
) {
}
