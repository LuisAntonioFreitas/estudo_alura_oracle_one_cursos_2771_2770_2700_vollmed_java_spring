package net.lanet.vollmed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.lanet.vollmed.domain.syssql.ISysSqlService;
import net.lanet.vollmed.domain.syssql.SysSpDtoRequest;
import net.lanet.vollmed.domain.syssql.SysSqlDtoRequest;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import net.lanet.vollmed.infra.utilities.RegexUtil;
import net.lanet.vollmed.infra.utilities.exportfiles.HandleExportFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@Tag(name = "Sql | Sp") // Swagger
@RequestMapping(path = "${api.config.path}")
public class SysSqlController {
    @Autowired
    DateTimeUtil dtu;

    @Autowired
    @Qualifier("sysSqlService")
    private ISysSqlService service;

    //    @Operation(hidden = true) // Swagger
    @SecurityRequirement(name = "bearer-key") // Swagger
    @Operation(summary = "consulta sql") // Swagger
    @PostMapping(path = {"/sql"})
    public ResponseEntity<?> executeSql(@RequestBody @Valid SysSqlDtoRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) String export) {
        String messageError = "";
        try {
            String sql = request.sql();
            String resultSql = request.result() != null ? request.result() : "";

            // Verifica comandos bloqueados
            if (sql.substring(0, 10).toUpperCase().contains("INSERT") ||
                sql.substring(0, 10).toUpperCase().contains("UPDATE") ||
                sql.substring(0, 10).toUpperCase().contains("DELETE") ||
                sql.substring(0, 10).toUpperCase().contains("TRUNCATE") ||
                sql.substring(0, 10).toUpperCase().contains("CREATE") ||
                sql.substring(0, 10).toUpperCase().contains("ALTER") ||
                sql.substring(0, 10).toUpperCase().contains("DBCC"))
            {
                messageError = "Existem comandos que não possuem autorização para utilização.";
                throw new RuntimeException("");
            }

            // Executa instrução SQL
            List<Map<String, Object>> results = service.executeSql(sql, resultSql);

            // Prapara retorno das informações
            List<Map<String, Object>> selectedResults = parseResults(resultSql, results);

            // Converte tipos de dados
            List<Map<String, Object>> convertedSelectedResults = convertResults(selectedResults);

            if (verifyExport("All", null, export, response, "Sql", convertedSelectedResults)) { return null; }
            return ResponseEntity.ok(convertedSelectedResults);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException((!messageError.isEmpty() ? messageError : "Erro ao executar a instrução SQL."), e);
        }
    }


    //    @Operation(hidden = true) // Swagger
    @SecurityRequirement(name = "bearer-key") // Swagger
    @Operation(summary = "executa stored procedures") // Swagger
    @PostMapping(path = {"/sp"})
    public ResponseEntity<?> executeStoredProcedure(@RequestBody @Valid SysSpDtoRequest request,
                                                    HttpServletResponse response,
                                                    @RequestParam(required = false) String export) {
        String messageError = "";
        try {
            String spName = request.sp();
            String resultSp = request.result() != null ? request.result() : "";
            Map<String, Object> params = request.param();

            // Executa Stored Procedure
            List<Map<String, Object>> results = service.executeStoredProcedure(spName, resultSp, params);

            // Prapara retorno das informações
            List<Map<String, Object>> selectedResults = parseResults(resultSp, results);

            // Converte tipos de dados
            List<Map<String, Object>> convertedSelectedResults = convertResults(selectedResults);

            if (verifyExport("All", null, export, response, "Sp", convertedSelectedResults)) { return null; }
            return ResponseEntity.ok(convertedSelectedResults);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException((!messageError.isEmpty() ? messageError : "Erro ao executar a Stored Procedure."), e);
        }
    }


    private static List<Map<String, Object>> parseResults(String resultSqlSp,
                                                          List<Map<String, Object>> results) {
        String resultVerify = resultSqlSp;
        if (resultVerify.length() > 0) {
            // Parse dos resultados para selecionar apenas os campos solicitados
            String[] fields = resultSqlSp.split(",");
            List<Map<String, Object>> selectedResults = new ArrayList<>();
            for (Map<String, Object> result : results) {
                Map<String, Object> selectedFields = new LinkedHashMap<>();
                for (String field : fields) {
                    String trimmedField = field.trim();
                    selectedFields.put(trimmedField, result.get(trimmedField));
                }
                selectedResults.add(selectedFields);
            }
            return selectedResults;
        } else {
            return results;
        }
    }

    private static List<Map<String, Object>> convertResults(List<Map<String, Object>> data) {
        for (Map<String, Object> dataMap : data) {
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                try {
                    // Tratando | Timestamp
                    if (entry.getValue() instanceof Timestamp) {
                        Timestamp timeStamp = (Timestamp) entry.getValue();
                        LocalDateTime localDateTime = timeStamp.toLocalDateTime();
                        String result = localDateTime.format(DateTimeUtil.formatter);
                        entry.setValue(result);
                    }
                } catch (Exception ignored) {}
            }
        }
        return data;
    }

    private Boolean verifyExport(String type, String search, String export, HttpServletResponse response,
                                 String item, List<Map<String, Object>> viewList) {
        if (export != null) {
            if (viewList.isEmpty()) {
                throw new EntityNotFoundException("Não existe conteúdo a ser exportado.");
            }
            String name = RegexUtil.normalizeStringLettersAndNumbers(item);
            HandleExportFile.execute(export, service, response, viewList,
                    String.format("%sList%s", name, type), String.format("Listagem | %s", item.toUpperCase()), null, name);
            return true;
        }
        return false;
    }

}
