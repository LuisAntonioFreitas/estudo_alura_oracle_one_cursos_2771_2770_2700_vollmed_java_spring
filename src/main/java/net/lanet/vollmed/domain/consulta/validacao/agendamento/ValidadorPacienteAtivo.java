package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;
import net.lanet.vollmed.domain.paciente.IPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements IValidadorAgendamentoConsulta {
    @Autowired
    private IPacienteRepository repository;
    @Override
    public void validar(ConsultaDtoAgendarRequest data) {
        var pacienteEstaAtivo = repository.findAtivoById(data.pacienteId());
        if (!pacienteEstaAtivo) {
            throw new ValidationException("Consulta não pode ser agendada com paciente excluído");
        }
    }
}
