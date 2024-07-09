package net.lanet.vollmed.domain.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioDtoSenhaRequest(
        @NotBlank(message = "Senha precisa ser preenchida.")
        @Size(max = 25, message = "Senha não pode conter mais do que 25 caracteres.")
        @JsonAlias("senha")
        String senha,

        @NotBlank(message = "Nova Senha precisa ser preenchida.")
        @Size(max = 25, message = "Nova Senha não pode conter mais do que 25 caracteres.")
        @JsonAlias("novasenha")
        String novaSenha,

        @NotBlank(message = "Confirma Nova Senha precisa ser preenchida.")
        @Size(max = 25, message = "Confirma Nova Senha não pode conter mais do que 25 caracteres.")
        @JsonAlias("confirmanovasenha")
        String confirmaNovaSenha
) {
}
