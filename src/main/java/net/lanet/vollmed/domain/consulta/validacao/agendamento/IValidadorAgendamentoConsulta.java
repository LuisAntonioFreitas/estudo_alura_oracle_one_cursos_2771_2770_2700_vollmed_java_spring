package net.lanet.vollmed.domain.consulta.validacao.agendamento;

import net.lanet.vollmed.domain.consulta.ConsultaDtoAgendarRequest;

public interface IValidadorAgendamentoConsulta {
    void validar(ConsultaDtoAgendarRequest data);
}
