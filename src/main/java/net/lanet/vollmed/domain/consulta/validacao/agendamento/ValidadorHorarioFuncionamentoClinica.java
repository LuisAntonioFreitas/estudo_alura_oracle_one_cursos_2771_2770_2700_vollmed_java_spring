package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements IValidadorAgendamentoConsulta {
    @Override
    public void validar(ConsultaDtoAgendarRequest data) {
        var dataConsulta = data.data();

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var depoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18;

        if (domingo || antesDaAberturaDaClinica || depoisDoEncerramentoDaClinica) {
            throw new ValidationException("Consulta fora do horário de funcionamento da clínica");
        }
    }
}
