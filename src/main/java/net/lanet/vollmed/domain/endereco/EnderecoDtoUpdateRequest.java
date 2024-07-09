package net.lanet.vollmed.domain.endereco;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import net.lanet.vollmed.infra.validation.NotBlankIfPresent;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record EnderecoDtoUpdateRequest(
        @NotBlankIfPresent(message = "Logradouro precisa ser preenchido.")
        @Size(max = 100, message = "Nome não pode conter mais do que 100 caracteres.")
        @JsonAlias("logradouro")
        String logradouro,

        @Size(max = 20, message = "Número não pode conter mais do que 20 caracteres.")
        @Column(name="numero", length=20)
        @JsonAlias("numero")
        String numero,

        @Size(max = 100, message = "Complemento não pode conter mais do que 100 caracteres.")
        @Column(name="complemento", length=100)
        @JsonAlias("complemento")
        String complemento,

        @NotBlankIfPresent(message = "Bairro precisa ser preenchido.")
        @Size(max = 100, message = "Bairro não pode conter mais do que 100 caracteres.")
        @JsonAlias("bairro")
        String bairro,

        @NotBlankIfPresent(message = "Cidade precisa ser preenchido.")
        @Size(max = 100, message = "Cidade não pode conter mais do que 100 caracteres.")
        @JsonAlias("bairro")
        String cidade,

        @NotBlankIfPresent(message = "UF precisa ser preenchido.")
        @Size(max = 2, message = "UF não pode conter mais do que 2 caracteres.")
        @JsonAlias("uf")
        String uf,

        @NotBlankIfPresent(message = "CEP precisa ser preenchido.")
        @Size(max = 10, message = "CEP não pode conter mais do que 10 caracteres.")
        @JsonAlias("cep")
        String cep
) {
}
