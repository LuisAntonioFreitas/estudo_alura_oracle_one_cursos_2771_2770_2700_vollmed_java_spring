package net.lanet.vollmed.domain.paciente;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import net.lanet.vollmed.domain.endereco.EnderecoDtoCreateRequest;
import org.hibernate.validator.constraints.br.CPF;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteDtoCreateRequest(
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

        @NotBlank(message = "CPF precisa ser preenchido.")
        @Size(max = 20, message = "CPF não pode conter mais do que 20 caracteres.")
        @CPF(message = "CPF informado não é válido.")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", message = "CPF deve corresponder ao formato 000.000.000-00.")
        @JsonAlias("cpf")
        String cpf,

        @NotNull
        @Valid
        @JsonAlias("endereco")
        EnderecoDtoCreateRequest endereco
) {
}
