package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;
import net.lanet.vollmed.domain.medico.IMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements IValidadorAgendamentoConsulta {
    @Autowired
    private IMedicoRepository repository;
    @Override
    public void validar(ConsultaDtoAgendarRequest data) {
        //escolha do medico é opcional
        if (data.medicoId() == null) {
            return;
        }

        var medicoEstaAtivo = repository.findAtivoById(data.medicoId());
        if (!medicoEstaAtivo) {
            throw new ValidationException("Consulta não pode ser agendada com médico excluído");
        }
    }
}
