package net.lanet.vollmed.domain.paciente;

import net.lanet.vollmed.infra.utilities.exportfiles.IHandleExportFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPacienteService extends IHandleExportFile {
    List<Paciente> findAll(String search);
    List<Paciente> findAllAtivoTrue(String search);
    Page<Paciente> pageFindAll(Pageable page, String search);
    Page<Paciente> pageFindAllAtivoTrue(Pageable page, String search);
    Optional<Paciente> findById(Long id);

    void delete(Paciente item);
    void ativa(Paciente item);
    Paciente update(Paciente item, PacienteDtoUpdateRequest data);
    Paciente create(PacienteDtoCreateRequest data);
}
