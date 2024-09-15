package net.lanet.vollmed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.lanet.vollmed.domain.consulta.*;
import net.lanet.vollmed.domain.medico.Medico;
import net.lanet.vollmed.domain.medico.MedicoDtoViewList;
import net.lanet.vollmed.infra.shared.ServiceCustom;
import net.lanet.vollmed.infra.utilities.ConvertsDataUtil;
import net.lanet.vollmed.infra.utilities.RegexUtil;
import net.lanet.vollmed.infra.utilities.UriBuilderUtil;
import net.lanet.vollmed.infra.utilities.exportfiles.HandleExportFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Consulta") // Swagger
@SecurityRequirement(name = "bearer-key") // Swagger
@RequestMapping(path = "${api.config.path}/consulta")
public class ConsultaController {
    @Autowired
    @Qualifier("consultaService")
    private IConsultaService service;

    @Autowired
    private ServiceCustom serviceCustom;

    private static final String ITEM = "consulta";
    private static final Object[] ITEMS = {"o","Consulta",ITEM};
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_NUMBER = 0;


    @Operation(summary = "lista " + ITEM + "s") // Swagger
    @GetMapping(path = {"/all"})
    public ResponseEntity<Object> findAll(HttpServletResponse response,
                                          @RequestParam(required = false) String search,
                                          @RequestParam(required = false) String export) {
//        if (verifyExport("All", search, export, response)) { return null; }
        if (serviceCustom.verifyExport("All", search, export, service, ConsultaDtoViewList::new,
                response, ITEMS)) { return null; }

        List<Consulta> listResult = service.findAll(search);
        List<ConsultaDtoViewList> viewList = ServiceCustom.listItens(listResult, ConsultaDtoViewList::new);

        return ResponseEntity.status(HttpStatus.OK).body(viewList);
    }

    @Operation(summary = "lista " + ITEM + "s com paginação") // Swagger
    @GetMapping(path = {""})
    public ResponseEntity<Object> pageFindAll(@PageableDefault(page = PAGE_NUMBER, size = PAGE_SIZE,
                                                               sort = {"data"}, direction = Sort.Direction.DESC)
                                              Pageable page,
                                              HttpServletResponse response,
                                              @RequestParam(required = false) String search,
                                              @RequestParam(required = false) String export) {
//        if (verifyExport("All", search, export, response)) { return null; }
        if (serviceCustom.verifyExport("All", search, export, service, ConsultaDtoViewList::new,
                response, ITEMS)) { return null; }

        Page<Consulta> listResult = service.pageFindAll(page, search);
        Page<ConsultaDtoViewList> viewList = ServiceCustom.pageListItens(listResult, ConsultaDtoViewList::new);

        return ResponseEntity.status(HttpStatus.OK).body(viewList);
    }

    @Operation(summary = "detalha " + ITEM) // Swagger
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id) {
//        Consulta item = findItem(id);
        Consulta item = ServiceCustom.findItem(id, service, ITEMS);
        ConsultaDtoView view = new ConsultaDtoView(item);

        return ResponseEntity.status(HttpStatus.OK).body(view);
    }

    @Operation(summary = "agenda " + ITEM) // Swagger
    @PostMapping(path = {""})
    public ResponseEntity<Object> agendar(@RequestBody @Valid ConsultaDtoAgendarRequest data,
                                          WebRequest request, UriComponentsBuilder uriBuilder) {
        Consulta item = service.agendar(data);
        ConsultaDtoView view = new ConsultaDtoView(item);
        URI uri = UriBuilderUtil.getUri(item.getId(), request, uriBuilder);

        return ResponseEntity.created(uri).body(view);
    }

    @Operation(summary = "cancela " + ITEM) // Swagger
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Object> cancelar(@PathVariable(value = "id") Long id,
                                           @RequestBody @Valid ConsultaDtoCancelarRequest data) {
//        Consulta item = findItem(id);
        Consulta item = ServiceCustom.findItem(id, service, ITEMS);
        service.cancelar(item, data);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


//    private Consulta findItem(Long id) {
//        Optional<Consulta> optional = service.findById(id);
//        if (optional.isEmpty()) {
//            throw new EntityNotFoundException(String.format("%s não foi encontrado", item));
//        }
//        return optional.get();
//    }
//    private List<ConsultaDtoViewList> listItens(List<Consulta> list) {
//        List<ConsultaDtoViewList> viewList = list
//                .stream()
//                .map(item -> new ConsultaDtoViewList(item))
//                .collect(Collectors.toList());
//        return viewList;
//    }
//    private Page<ConsultaDtoViewList> pageListItens(Page<Consulta> list) {
//        Page<ConsultaDtoViewList> viewList = list
//                .map(item -> new ConsultaDtoViewList(item));
//        return viewList;
//    }
//
//
//    private Boolean verifyExport(String type, String search, String export, HttpServletResponse response) {
//        if (export != null) {
//            String defineSearch = null;
//            List<Consulta> listResult = null;
//            if (type.equalsIgnoreCase("All")) {
//                listResult = service.findAll(search);
//            }
//            List<ConsultaDtoViewList> viewList = listItens(listResult);
//            if (viewList.isEmpty()) {
//                throw new EntityNotFoundException("Não existe conteúdo a ser exportado.");
//            }
//            String name = RegexUtil.normalizeStringLettersAndNumbers(item);
//            List<Map<String, Object>> toListOfMaps = ConvertsDataUtil.convertToListOfMaps(viewList);
//            HandleExportFile.execute(export, service, response, toListOfMaps,
//                    String.format("%sList%s", name, type), String.format("Listagem | %s", item), defineSearch, name);
//
//            return true;
//        }
//        return false;
//    }

}
