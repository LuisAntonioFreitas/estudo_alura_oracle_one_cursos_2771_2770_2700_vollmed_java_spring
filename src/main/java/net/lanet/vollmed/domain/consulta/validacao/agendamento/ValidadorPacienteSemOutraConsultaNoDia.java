package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;
import net.lanet.vollmed.domain.consulta.IConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteSemOutraConsultaNoDia implements IValidadorAgendamentoConsulta {
    @Autowired
    private IConsultaRepository repository;
    @Override
    public void validar(ConsultaDtoAgendarRequest data) {
        var dataConsulta = data.data();

        var primeiroHorario = dataConsulta.withHour(7);
        var ultimoHorario = dataConsulta.withHour(18);

        var pacientePossuiOutraConsultaNoDia =
                repository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(data.pacienteId(), primeiroHorario, ultimoHorario);
        if (pacientePossuiOutraConsultaNoDia) {
            throw new ValidationException("Paciente já possui uma consulta agendada nesse dia");
        }
    }
}
