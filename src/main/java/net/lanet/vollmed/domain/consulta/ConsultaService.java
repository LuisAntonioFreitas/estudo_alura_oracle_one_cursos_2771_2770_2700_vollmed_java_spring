package net.lanet.vollmed.domain.consulta;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import net.lanet.vollmed.domain.consulta.validacao.agendamento.IValidadorAgendamentoConsulta;
import net.lanet.vollmed.domain.consulta.validacao.cancelamento.IValidadorCancelamentoConsulta;
import net.lanet.vollmed.domain.medico.Especialidade;
import net.lanet.vollmed.domain.medico.IMedicoRepository;
import net.lanet.vollmed.domain.medico.Medico;
import net.lanet.vollmed.domain.paciente.IPacienteRepository;
import net.lanet.vollmed.domain.paciente.Paciente;
import net.lanet.vollmed.infra.shared.JpaRepositoryCustom;
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
import java.util.Random;

@Service
@Transactional
public class ConsultaService implements IConsultaService {
    @Autowired
    private IConsultaRepository repository;
    @Autowired
    private IMedicoRepository repositoryMedico;
    @Autowired
    private IPacienteRepository repositoryPaciente;
    @Autowired
    private List<IValidadorAgendamentoConsulta> validadoresAgendamento;
    @Autowired
    private List<IValidadorCancelamentoConsulta> validadoresCancelamento;
//    @Autowired
//    private TemplateGenericExport template;

    @Override
    @Transactional(readOnly = true)
    public List<Consulta> findAll(String search) {
        Sort sortBy = Sort.by(new Sort.Order(Sort.Direction.DESC, "data").ignoreCase());
        List<Consulta> list;
        if (search == null) {
            list = repository.findAll(sortBy);
        } else {
            list = repository.findAll(sortBy); //.findAllFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Consulta> pageFindAll(Pageable page, String search) {
        Page<Consulta> list;
        if (search == null) {
            list = repository.findAll(page);
        } else {
            list = repository.findAll(page); //.pageFindAllFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consulta> findById(Long id) {
        Optional<Consulta> item = repository.findById(id);
        return item;
    }

    @Override
    public Consulta agendar(ConsultaDtoAgendarRequest data) {
//        Paciente paciente = findPaciente(data.pacienteId(), null);
//        Medico medico = findMedico(data.medicoId(), null);

        Paciente paciente = JpaRepositoryCustom.findEntityNotNull(
                data.pacienteId(), null, repositoryPaciente, new Object[] {"o","Paciente"});
        Medico medico = JpaRepositoryCustom.findEntityNull(
                data.medicoId(), null, repositoryMedico, new Object[] {"o","Médico"});

        validadoresAgendamento.forEach(v -> v.validar(data));

        if (medico == null) {
            medico = sortearMedico(data);
        }

        Consulta item = new Consulta(data, medico, paciente);
        repository.save(item);
        return item;
    }
//    private Paciente findPaciente(Long id, Paciente paciente) {
//        if (id == null && paciente != null) { return paciente; }
//        String item = "Paciente";
//        String mensagem = String.format("%s não foi encontrado", item);
//        if (id == null) { throw new EntityNotFoundException(mensagem); } // return null; }
//        Optional<Paciente> optional = repositoryPaciente.findById(id);
//        if (optional.isEmpty()) {
//            throw new EntityNotFoundException(mensagem);
//        }
//        return optional.get();
//    }
//    private Medico findMedico(Long id, Medico medico) {
//        if (id == null && medico != null) { return medico; }
//        String item = "Médico";
//        String mensagem = String.format("%s não foi encontrado", item);
//        if (id == null) { return null; }
//        Optional<Medico> optional = repositoryMedico.findById(id);
//        if (optional.isEmpty()) {
//            throw new EntityNotFoundException(mensagem);
//        }
//        return optional.get();
//    }

    @Override
    public void cancelar(Consulta item, ConsultaDtoCancelarRequest data) {
        validadoresCancelamento.forEach(v -> v.validar(item, data));

        item.cancelar(data);
        repository.save(item);
    }

    private Medico sortearMedico(ConsultaDtoAgendarRequest data) {
        System.out.println("0.data.especialidade(): " + data.especialidade());
        if (data.especialidade() == null) {
            throw new ValidationException("Especialidade é obrigatória quando médico não for escolhido!");
        }
        List<Medico> lista = repositoryMedico.sortearMedicoAleatorioLivreNaData(
                Especialidade.valueOf(data.especialidade()), data.data());
        if (lista.isEmpty()) {
            throw new ValidationException("Não há nenhum médico disponível para a especialidade escolhida!");
        }
        return lista.get(new Random().nextInt(lista.size()));
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
