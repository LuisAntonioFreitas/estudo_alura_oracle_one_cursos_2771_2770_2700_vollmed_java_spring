package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAntecedenciaAgendamento")
public class ValidadorHorarioAntecedencia implements IValidadorAgendamentoConsulta {
    @Override
    public void validar(ConsultaDtoAgendarRequest data) {
        var dataConsulta = data.data();

        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new ValidationException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
        }
    }
}
