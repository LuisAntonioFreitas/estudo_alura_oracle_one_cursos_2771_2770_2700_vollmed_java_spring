package net.lanet.vollmed.domain.consulta;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lanet.vollmed.domain.medico.MedicoDtoViewList;
import net.lanet.vollmed.domain.paciente.PacienteDtoViewList;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id"})
public record ConsultaDtoViewList(
        Long id,
        String data,
        MotivoCancelamento motivoCancelamento,
        MedicoDtoViewList medico,
        PacienteDtoViewList paciente
) {
    public ConsultaDtoViewList(Consulta entity) {
        this(
                entity.getId(),
                entity.getData().format(DateTimeUtil.formatter),
                entity.getMotivoCancelamento(),
                new MedicoDtoViewList(entity.getMedico()),
                new PacienteDtoViewList(entity.getPaciente())
        );
    }
}
