package net.lanet.vollmed.infra.shared;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.ConvertsDataUtil;
import net.lanet.vollmed.infra.utilities.RegexUtil;
import net.lanet.vollmed.infra.utilities.exportfiles.HandleExportFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ServiceCustom {
    @Autowired
    private HandleExportFile handleExportFile;

    public interface MethodsStandard<T> {
        default Optional<T> findById(Long id) { return Optional.empty(); };

        default List<T> findAll(String search) { return null; };
        default List<T> findAllAtivoTrue(String search) { return null; };

        default List<T> findAll(Long id, String search) { return null; };
        default List<T> findAllAtivoTrue(Long id, String search) { return null; };
    }

    public static <T> T findItem(Long id, MethodsStandard<T> service, Object[] item) {
        String message = String.format("%s não foi encontrad%s", item[1], item[0]);
        Optional<T> optional = service.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException(message);
        }
        return optional.get();
    }

    public static <T, R> List<R> listItens(List<T> list, Function<T, R> mapper) {
        List<R> viewList = list
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
        return viewList;
    }

    public static <T, R> Page<R> pageListItens(Page<T> list, Function<T, R> mapper) {
        Page<R> viewList = list
                .map(mapper);
        return viewList;
    }



    public <T, R> Boolean verifyExport(String type, String search, String export,
                                       MethodsStandard<T> service, Function<T, R> viewMapper,
                                       HttpServletResponse response, Object[] item) {
        if (export != null) {
            String defineSearch = null;
            if (search != null) { defineSearch = String.format("Search: %s", search); }
            if (type.equalsIgnoreCase("Ativo")) {
                if (defineSearch != null) { defineSearch = String.format("%s  |  Ativo: True", defineSearch); }
                else { defineSearch = "Ativo: True"; }
            }

            List<T> listResult = null;
            if (type.equalsIgnoreCase("All")) {
                listResult = service.findAll(search);
            }
            if (type.equalsIgnoreCase("Ativo")) {
                listResult = service.findAllAtivoTrue(search);
            }
            List<R> viewList = listItens(listResult, viewMapper);
            if (viewList.isEmpty()) {
                throw new EntityNotFoundException("Não existe conteúdo a ser exportado.");
            }
            String name = RegexUtil.normalizeStringLettersAndNumbers(item[1].toString());
            String filename = String.format("%sList%s", name, type);
            String title = String.format("Listagem | %s", item[1]);
            List<Map<String, Object>> toListOfMaps = ConvertsDataUtil.convertToListOfMaps(viewList);
            handleExportFile.execute(export, response, toListOfMaps, filename, title, defineSearch, name);

            return true;
        }
        return false;
    }
    public <T, R> Boolean verifyExport(String type, Long id, String search, String export,
                                       MethodsStandard<T> service, Function<T, R> viewMapper,
                                       HttpServletResponse response, Object[] item) {
        if (export != null) {
            String defineSearch = null;
            if (search != null) { defineSearch = String.format("Search: %s", search); }
            if (type.equalsIgnoreCase("Ativo")) {
                if (defineSearch != null) { defineSearch = String.format("%s  |  Ativo: True", defineSearch); }
                else { defineSearch = "Ativo: True"; }
            }

            List<T> listResult = null;
            if (type.equalsIgnoreCase("All")) {
                listResult = service.findAll(id, search);
            }
            if (type.equalsIgnoreCase("Ativo")) {
                listResult = service.findAllAtivoTrue(id, search);
            }
            List<R> viewList = listItens(listResult, viewMapper);
            if (viewList.isEmpty()) {
                throw new EntityNotFoundException("Não existe conteúdo a ser exportado.");
            }
            String name = RegexUtil.normalizeStringLettersAndNumbers(item[1].toString());
            String filename = String.format("%sList%s", name, type);
            String title = String.format("Listagem | %s", item[1]);
            List<Map<String, Object>> toListOfMaps = ConvertsDataUtil.convertToListOfMaps(viewList);
            handleExportFile.execute(export, response, toListOfMaps, filename, title, defineSearch, name);

            return true;
        }
        return false;
    }
    public Boolean verifyExport(String type, String search, String export,
                                List<Map<String, Object>> viewList,
                                HttpServletResponse response, String item) {
        if (export != null) {
            if (viewList.isEmpty()) {
                throw new EntityNotFoundException("Não existe conteúdo a ser exportado.");
            }
            String name = RegexUtil.normalizeStringLettersAndNumbers(item);
            String filename = String.format("%sList%s", name, type);
            String title = String.format("Listagem | %s", item.toUpperCase());
            handleExportFile.execute(export, response, viewList, filename, title, null, name);

            return true;
        }
        return false;
    }
}
