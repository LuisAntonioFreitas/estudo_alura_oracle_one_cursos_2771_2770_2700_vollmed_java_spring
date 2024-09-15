package net.lanet.vollmed.domain.consulta;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.lanet.vollmed.domain.medico.Especialidade;
import net.lanet.vollmed.infra.validation.EnumValueConstraint;

import java.time.LocalDateTime;

//@Hidden // Swagger
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaDtoAgendarRequest(
        @NotNull(message = "Id do Paciente precisa ser informado.")
        @JsonAlias("pacienteId")
        Long pacienteId,

        @JsonAlias("medicoId")
        Long medicoId,

        @NotNull(message = "Data precisa ser preenchida.")
        @Future(message = "Data precisa ser válida.")
        @JsonAlias("data")
        LocalDateTime data,

        @EnumValueConstraint(enumClass = Especialidade.class, message = "Especialidade informada não é válida.")
        @JsonAlias("especialidade")
        String especialidade
) {
}
