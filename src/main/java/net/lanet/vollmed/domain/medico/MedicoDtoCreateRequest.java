package net.lanet.vollmed.domain.medico;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import net.lanet.vollmed.domain.endereco.EnderecoDtoCreateRequest;
import net.lanet.vollmed.infra.validation.EnumValueConstraint;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoDtoCreateRequest(
        @NotBlank(message = "Nome precisa ser preenchido.")
        @Size(max = 255, message = "Nome não pode conter mais do que 255 caracteres.")
        @JsonAlias("nome")
        String nome,

        @NotBlank(message = "Email precisa ser preenchido.")
        @Size(max = 255, message = "Email não pode conter mais do que 255 caracteres.")
        @Email(message = "Email precisa ser preenchido corretamente.")
        @JsonAlias("email")
        String email,

        @NotBlank(message = "Telefone precisa ser preenchido.")
        @Size(max = 50, message = "Telefone não pode conter mais do que 50 caracteres.")
        @JsonAlias("telefone")
        String telefone,

        @NotBlank(message = "CRM precisa ser preenchido.")
        @Size(max = 20, message = "CRM não pode conter mais do que 20 caracteres.")
        @Pattern(regexp = "\\d{4,6}", message = "CRM deve corresponder ao formato 000000.")
        @JsonAlias("crm")
        String crm,

        @NotBlank(message = "Especialidade precisa ser preenchida.")
        @EnumValueConstraint(enumClass = Especialidade.class, message = "Especialidade informada não é válida.")
        @JsonAlias("especialidade")
        String especialidade,

        @NotNull
        @Valid
        @JsonAlias("endereco")
        EnderecoDtoCreateRequest endereco
) {
}
