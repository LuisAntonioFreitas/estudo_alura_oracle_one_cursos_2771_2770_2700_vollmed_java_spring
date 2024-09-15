package net.lanet.vollmed.domain.consulta;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import net.lanet.vollmed.infra.validation.EnumValueConstraint;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaDtoCancelarRequest(
        @NotBlank(message = "Motivo precisa ser preenchido.")
        @EnumValueConstraint(enumClass = MotivoCancelamento.class, message = "Motivo informado não é válido.")
        @JsonAlias("motivo")
        String motivoCancelamento
) {
}
