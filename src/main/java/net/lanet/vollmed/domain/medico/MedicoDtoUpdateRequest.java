package net.lanet.vollmed.domain.medico;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import net.lanet.vollmed.domain.endereco.EnderecoDtoUpdateRequest;
import net.lanet.vollmed.infra.validation.EnumValueConstraint;
import net.lanet.vollmed.infra.validation.NotBlankIfPresent;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoDtoUpdateRequest(
        @NotBlankIfPresent(message = "Nome precisa ser preenchido.")
        @Size(max = 255, message = "Nome não pode conter mais do que 255 caracteres.")
        @JsonAlias("nome")
        String nome,

        @NotBlankIfPresent(message = "Email precisa ser preenchido.")
        @Size(max = 255, message = "Email não pode conter mais do que 255 caracteres.")
        @Email(message = "Email precisa ser preenchido corretamente.")
        @JsonAlias("email")
        String email,

        @NotBlankIfPresent(message = "Telefone precisa ser preenchido.")
        @Size(max = 50, message = "Telefone não pode conter mais do que 50 caracteres.")
        @JsonAlias("telefone")
        String telefone,

        @NotBlankIfPresent(message = "Especialidade precisa ser preenchida.")
        @EnumValueConstraint(enumClass = Especialidade.class, message = "Especialidade informada não é válida.")
        @JsonAlias("especialidade")
        String especialidade,

        @Valid
        @JsonAlias("endereco")
        EnderecoDtoUpdateRequest endereco
) {
}
