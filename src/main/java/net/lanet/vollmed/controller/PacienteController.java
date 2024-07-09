package net.lanet.vollmed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.lanet.vollmed.domain.paciente.*;
import net.lanet.vollmed.infra.utilities.ConvertsDataUtil;
import net.lanet.vollmed.infra.utilities.RegexUtil;
import net.lanet.vollmed.infra.utilities.UriBuilderUtil;
import net.lanet.vollmed.infra.utilities.exportfiles.HandleExportFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Paciente") // Swagger
@SecurityRequirement(name = "bearer-key") // Swagger
@RequestMapping(path = "${api.config.path}/paciente")
public class PacienteController {
    @Autowired
    @Qualifier("pacienteService")
    private IPacienteService service;

    private final String item = "Paciente";
    private final String itemLowerCase = "paciente";
    private final int pageSize = 10;
    private final int pageNumber = 0;


    @Operation(summary = "lista " + itemLowerCase + "s") // Swagger
    @GetMapping(path = {"/all"})
    public ResponseEntity<Object> findAll(HttpServletResponse response,
                                          @RequestParam(required = false) String search,
                                          @RequestParam(required = false) String export) {
        if (verifyExport("All", search, export, response)) { return null; }

        List<Paciente> listResult = service.findAll(search);
        List<PacienteDtoViewList> viewList = listItens(listResult);

        return ResponseEntity.status(HttpStatus.OK).body(viewList);
    }

    @Operation(summary = "lista " + itemLowerCase + "s ativos") // Swagger
    @GetMapping(path = {"/all/ativo"})
    public ResponseEntity<Object> findAllAtivoTrue(HttpServletResponse response,
                                                   @RequestParam(required = false) String search,
                                                   @RequestParam(required = false) String export) {
        if (verifyExport("Ativo", search, export, response)) { return null; }

        List<Paciente> listResult = service.findAllAtivoTrue(search);
        List<PacienteDtoViewList> viewList = listItens(listResult);

        return ResponseEntity.status(HttpStatus.OK).body(viewList);
    }

    @Operation(summary = "lista " + itemLowerCase + "s com paginação") // Swagger
    @GetMapping(path = {""})
    public ResponseEntity<Object> pageFindAll(@PageableDefault(page = pageNumber, size = pageSize, sort = {"nome"})
                                              Pageable page,
                                              HttpServletResponse response,
                                              @RequestParam(required = false) String search,
                                              @RequestParam(required = false) String export) {
        if (verifyExport("All", search, export, response)) { return null; }

        Page<Paciente> listResult = service.pageFindAll(page, search);
        Page<PacienteDtoViewList> viewList = pageListItens(listResult);

        return ResponseEntity.status(HttpStatus.OK).body(viewList);
    }

    @Operation(summary = "lista " + itemLowerCase + "s ativos com paginação") // Swagger
    @GetMapping(path = {"/ativo"})
    public ResponseEntity<Object> pageFindAllAtivoTrue(@PageableDefault(page = pageNumber, size = pageSize, sort = {"nome"})
                                                       Pageable page,
                                                       HttpServletResponse response,
                                                       @RequestParam(required = false) String search,
                                                       @RequestParam(required = false) String export) {
        if (verifyExport("Ativo", search, export, response)) { return null; }

        Page<Paciente> listResult = service.pageFindAllAtivoTrue(page, search);
        Page<PacienteDtoViewList> viewList = pageListItens(listResult);

        return ResponseEntity.status(HttpStatus.OK).body(viewList);
    }

    @Operation(summary = "detalha " + itemLowerCase) // Swagger
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id) {
        Paciente item = findItem(id);
        PacienteDtoView view = new PacienteDtoView(item);

        return ResponseEntity.status(HttpStatus.OK).body(view);
    }

    @Operation(summary = "desativa " + itemLowerCase) // Swagger
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id) {
        Paciente item = findItem(id);
        service.delete(item);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "ativa " + itemLowerCase) // Swagger
    @PatchMapping(path = {"/ativo/{id}"})
    public ResponseEntity<Object> ativa(@PathVariable(value = "id") Long id) {
        Paciente item = findItem(id);
        service.ativa(item);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "atualiza " + itemLowerCase) // Swagger
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody @Valid PacienteDtoUpdateRequest data) {
        Paciente item = service.update(findItem(id), data);
        PacienteDtoView view = new PacienteDtoView(item);

        return ResponseEntity.status(HttpStatus.OK).body(view);
    }

    @Operation(summary = "cadastra " + itemLowerCase) // Swagger
    @PostMapping(path = {""})
    public ResponseEntity<Object> create(@RequestBody @Valid PacienteDtoCreateRequest data,
                                         WebRequest request, UriComponentsBuilder uriBuilder) {
        Paciente item = service.create(data);
        PacienteDtoView view = new PacienteDtoView(item);
        URI uri = UriBuilderUtil.getUri(item.getId(), request, uriBuilder);

        return ResponseEntity.created(uri).body(view);
    }


    private Paciente findItem(Long id) {
        Optional<Paciente> optional = service.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException(String.format("%s não foi encontrado", item));
        }
        return optional.get();
    }
    private List<PacienteDtoViewList> listItens(List<Paciente> list) {
        List<PacienteDtoViewList> viewList = list
                .stream()
                .map(item -> new PacienteDtoViewList(item))
                .collect(Collectors.toList());
        return viewList;
    }
    private Page<PacienteDtoViewList> pageListItens(Page<Paciente> list) {
        Page<PacienteDtoViewList> viewList = list
                .map(item -> new PacienteDtoViewList(item));
        return viewList;
    }


    private Boolean verifyExport(String type, String search, String export, HttpServletResponse response) {
        if (export != null) {
            String defineSearch = null;
            if (search != null) { defineSearch = String.format("Search: %s", search); }
            if (type.equalsIgnoreCase("Ativo")) {
                if (defineSearch != null) { defineSearch = String.format("%s  |  Ativo: True", defineSearch); }
                else { defineSearch = "Ativo: True"; }
            }

            List<Paciente> listResult = null;
            if (type.equalsIgnoreCase("All")) {
                listResult = service.findAll(search);
            }
            if (type.equalsIgnoreCase("Ativo")) {
                listResult = service.findAllAtivoTrue(search);
            }
            List<PacienteDtoViewList> viewList = listItens(listResult);
            if (viewList.isEmpty()) {
                throw new EntityNotFoundException("Não existe conteúdo a ser exportado.");
            }
            String name = RegexUtil.normalizeStringLettersAndNumbers(item);
            List<Map<String, Object>> toListOfMaps = ConvertsDataUtil.convertToListOfMaps(viewList);
            HandleExportFile.execute(export, service, response, toListOfMaps,
                    String.format("%sList%s", name, type), String.format("Listagem | %s", item), defineSearch, name);

            return true;
        }
        return false;
    }

}
