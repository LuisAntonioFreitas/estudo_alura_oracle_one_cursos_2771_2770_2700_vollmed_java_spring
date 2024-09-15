package net.lanet.vollmed.domain.usuario;

import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.PasswordEncoderUtil;
import net.lanet.vollmed.infra.utilities.exportfiles.TemplateGenericExport;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository repository;
    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;
//    @Autowired
//    private TemplateGenericExport template;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll(String search) {
        Sort sortBy = Sort.by(new Sort.Order(Sort.Direction.ASC, "nome").ignoreCase());
        List<Usuario> list;
        if (search == null) {
            list = repository.findAll(sortBy);
        } else {
            list = repository.findAllFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllAtivoTrue(String search) {
        List<Usuario> list;
        if (search == null) {
            list = repository.findByAtivoTrueOrderByNomeAsc();
        } else {
            list = repository.findAllAtivoTrueFilter(search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> pageFindAll(Pageable page, String search) {
        Page<Usuario> list;
        if (search == null) {
            list = repository.findAll(page);
        } else {
            list = repository.pageFindAllFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> pageFindAllAtivoTrue(Pageable page, String search) {
        Page<Usuario> list;
        if (search == null) {
            list = repository.findByAtivoTrueOrderByNomeAsc(page);
        } else {
            list = repository.pageFindAllAtivoTrueFilter(page, search);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        Optional<Usuario> item = repository.findById(id);
        return item;
    }

    @Override
    public void delete(Usuario item) {
        item.delete();
        repository.save(item);
    }

    @Override
    public void ativa(Usuario item) {
        item.ativa();
        repository.save(item);
    }

    @Override
    public Usuario update(Usuario item, UsuarioDtoUpdateRequest data) {
        item.update(data);
        repository.save(item);
        return item;
    }

    @Override
    public Usuario senha(Usuario item, UsuarioDtoSenhaRequest data) {
        if (item.getAtivo()) {
            if (passwordEncoderUtil.matches(data.senha(), item.getSenha())) {
                if (!data.novaSenha().equals(data.confirmaNovaSenha())) {
                    return null;
                }
            } else { throw new BadCredentialsException(""); }
        } else { throw new AccessDeniedException(""); }
        item.setSenha(passwordEncoderUtil.encode(data.novaSenha()));
        repository.save(item);
        return item;
    }

    @Override
    public Usuario create(UsuarioDtoCreateRequest data) {
        Usuario item = new Usuario(data);
        item.setSenha(passwordEncoderUtil.encode(item.getSenha()));
        repository.save(item);
        return item;
    }

    @Transactional(readOnly = true)
    public Usuario login(String login, String senha) {
        Usuario item = repository.findFirstTop1ByLoginOrEmail(login, login);
        if (item != null) {
            if (item.getAtivo()) {
                if (passwordEncoderUtil.matches(senha, item.getSenha())) { return item; }
            } else { throw new AccessDeniedException(""); }
        }
        return null;
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
