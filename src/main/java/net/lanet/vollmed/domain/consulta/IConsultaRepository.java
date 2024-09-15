package net.lanet.vollmed.domain.consulta;

import net.lanet.vollmed.infra.shared.JpaRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IConsultaRepository extends JpaRepositoryCustom.MethodsStandard<Consulta> {
    boolean existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(Long pacienteId, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);

    boolean existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(Long medicoId, LocalDateTime data);
}
