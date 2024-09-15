package net.lanet.vollmed.domain.consulta.validacao.cancelamento;

import net.lanet.vollmed.domain.consulta.Consulta;
import net.lanet.vollmed.domain.consulta.ConsultaDtoCancelarRequest;

public interface IValidadorCancelamentoConsulta {
    void validar(Consulta item, ConsultaDtoCancelarRequest data);
}
