package net.lanet.vollmed.domain.consulta.validacao.cancelamento;

import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.Consulta;
import net.lanet.vollmed.domain.consulta.ConsultaDtoCancelarRequest;
import net.lanet.vollmed.domain.consulta.IConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAntecedenciaCancelamento")
public class ValidadorHorarioAntecedencia implements IValidadorCancelamentoConsulta {
    @Autowired
    private IConsultaRepository repository;
    @Override
    public void validar(Consulta item, ConsultaDtoCancelarRequest data) {
        var consulta = repository.getReferenceById(item.getId());
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidationException("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
        }
    }
}
