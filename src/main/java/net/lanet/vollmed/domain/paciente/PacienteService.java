package net.lanet.vollmed.domain.paciente;

import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.exportfiles.TemplateGenericExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class PacienteService implements IPacienteService {
    @Autowired
    private IPacienteRepository repository;
    @Autowired
    private TemplateGenericExport template;

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> findAll(String search) {
        Sort sortBy = Sort.by(new Sort.Order(Sort.Direction.ASC, "nome").ignoreCase());
        List<Paciente> list;
        if (search == null) {
            list = repository.findAll(sortBy);
        } else {
            list = repository.findAllFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> findAllAtivoTrue(String search) {
        List<Paciente> list;
        if (search == null) {
            list = repository.findByAtivoTrueOrderByNomeAsc();
        } else {
            list = repository.findAllAtivoTrueFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Paciente> pageFindAll(Pageable page, String search) {
        Page<Paciente> list;
        if (search == null) {
            list = repository.findAll(page);
        } else {
            list = repository.pageFindAllFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Paciente> pageFindAllAtivoTrue(Pageable page, String search) {
        Page<Paciente> list;
        if (search == null) {
            list = repository.findByAtivoTrueOrderByNomeAsc(page);
        } else {
            list = repository.pageFindAllAtivoTrueFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> findById(Long id) {
        Optional<Paciente> item = repository.findById(id);
        return item;
    }

    @Override
    public void delete(Paciente item) {
        item.delete();
        repository.save(item);
    }

    @Override
    public void ativa(Paciente item) {
        item.ativa();
        repository.save(item);
    }

    @Override
    public Paciente update(Paciente item, PacienteDtoUpdateRequest data) {
        item.update(data);
        repository.save(item);
        return item;
    }

    @Override
    public Paciente create(PacienteDtoCreateRequest data) {
        Paciente item = new Paciente(data);
        repository.save(item);
        return item;
    }



    @Override
    public void generateXLS(HttpServletResponse response, List<Map<String, Object>> list, String fileName,
                            String title, String filter, String tabName) {
        // Excel
        template.generateXLS(response, list, fileName, title, filter, tabName);
    }
    @Override
    public void generateCSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        template.generateCSV(response, list, fileName);
    }
    @Override
    public void generateTSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        template.generateTSV(response, list, fileName);
    }
    @Override
    public void generatePDF(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        template.generatePDF(response, list, fileName);
    }
}
