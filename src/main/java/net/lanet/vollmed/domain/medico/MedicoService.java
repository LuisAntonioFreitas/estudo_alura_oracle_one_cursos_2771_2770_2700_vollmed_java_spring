package net.lanet.vollmed.domain.medico;

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
public class MedicoService implements IMedicoService {
    @Autowired
    private IMedicoRepository repository;
//    @Autowired
//    private TemplateGenericExport template;

    @Override
    @Transactional(readOnly = true)
    public List<Medico> findAll(String search) {
        Sort sortBy = Sort.by(new Sort.Order(Sort.Direction.ASC, "nome").ignoreCase());
        List<Medico> list;
        if (search == null) {
            list = repository.findAll(sortBy);
        } else {
            list = repository.findAllFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> findAllAtivoTrue(String search) {
        List<Medico> list;
        if (search == null) {
            list = repository.findByAtivoTrueOrderByNomeAsc();
        } else {
            list = repository.findAllAtivoTrueFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medico> pageFindAll(Pageable page, String search) {
        Page<Medico> list;
        if (search == null) {
            list = repository.findAll(page);
        } else {
            list = repository.pageFindAllFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medico> pageFindAllAtivoTrue(Pageable page, String search) {
        Page<Medico> list;
        if (search == null) {
            list = repository.findByAtivoTrueOrderByNomeAsc(page);
        } else {
            list = repository.pageFindAllAtivoTrueFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medico> findById(Long id) {
        Optional<Medico> item = repository.findById(id);
        return item;
    }

    @Override
    public void delete(Medico item) {
        item.delete();
        repository.save(item);
    }

    @Override
    public void ativa(Medico item) {
        item.ativa();
        repository.save(item);
    }

    @Override
    public Medico update(Medico item, MedicoDtoUpdateRequest data) {
        item.update(data);
        repository.save(item);
        return item;
    }

    @Override
    public Medico create(MedicoDtoCreateRequest data) {
        Medico item = new Medico(data);
        repository.save(item);
        return item;
    }



//    @Override
//    public void generateXLS(HttpServletResponse response, List<Map<String, Object>> list, String fileName,
//                            String title, String filter, String tabName) {
//        // Excel
//        template.generateXLS(response, list, fileName, title, filter, tabName);
//    }
//    @Override
//    public void generateCSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
//        template.generateCSV(response, list, fileName);
//    }
//    @Override
//    public void generateTSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
//        template.generateTSV(response, list, fileName);
//    }
//    @Override
//    public void generatePDF(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
//        template.generatePDF(response, list, fileName);
//    }
}
