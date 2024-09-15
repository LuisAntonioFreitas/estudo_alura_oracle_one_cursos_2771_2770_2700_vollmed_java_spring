package net.lanet.vollmed.domain.consulta;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lanet.vollmed.domain.medico.MedicoDtoView;
import net.lanet.vollmed.domain.paciente.PacienteDtoView;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id"})
public record ConsultaDtoView(
        Long id,
        String data,
        MotivoCancelamento motivoCancelamento,
        String createdAt,
        String updatedAt,
        MedicoDtoView medico,
        PacienteDtoView paciente
) {
    public ConsultaDtoView(Consulta entity) {
        this(
                entity.getId(),
                entity.getData().format(DateTimeUtil.formatter),
                entity.getMotivoCancelamento(),
                entity.getCreatedAt().format(DateTimeUtil.formatter),
                entity.getUpdatedAt().format(DateTimeUtil.formatter),
                new MedicoDtoView(entity.getMedico()),
                new PacienteDtoView(entity.getPaciente())
        );
    }
}
