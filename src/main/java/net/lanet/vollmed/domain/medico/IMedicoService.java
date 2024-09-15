package net.lanet.vollmed.domain.medico;

import net.lanet.vollmed.infra.shared.ServiceCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMedicoService extends ServiceCustom.MethodsStandard<Medico> {
    List<Medico> findAll(String search);
    List<Medico> findAllAtivoTrue(String search);
    Page<Medico> pageFindAll(Pageable page, String search);
    Page<Medico> pageFindAllAtivoTrue(Pageable page, String search);
    Optional<Medico> findById(Long id);

    void delete(Medico item);
    void ativa(Medico item);
    Medico update(Medico item, MedicoDtoUpdateRequest data);
    Medico create(MedicoDtoCreateRequest data);
}
