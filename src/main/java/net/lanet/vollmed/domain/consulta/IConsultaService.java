package net.lanet.vollmed.domain.consulta;

import net.lanet.vollmed.infra.shared.ServiceCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IConsultaService extends ServiceCustom.MethodsStandard<Consulta> {
    List<Consulta> findAll(String search);
    Page<Consulta> pageFindAll(Pageable page, String search);
    Optional<Consulta> findById(Long id);

    Consulta agendar(ConsultaDtoAgendarRequest data);
    void cancelar(Consulta item, ConsultaDtoCancelarRequest data);
}
