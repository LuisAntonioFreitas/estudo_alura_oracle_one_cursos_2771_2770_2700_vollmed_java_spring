package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;
import net.lanet.vollmed.domain.consulta.IConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements IValidadorAgendamentoConsulta {
    @Autowired
    private IConsultaRepository repository;
    @Override
    public void validar(ConsultaDtoAgendarRequest data) {
        var medicoPossuiOutraConsultaNoMesmoHorario =
                repository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(data.medicoId(), data.data());
        if (medicoPossuiOutraConsultaNoMesmoHorario) {
            throw new ValidationException("Médico já possui outra consulta agendada nesse mesmo horário");
        }
    }
}
