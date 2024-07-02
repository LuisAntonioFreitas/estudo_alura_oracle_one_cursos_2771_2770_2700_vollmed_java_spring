package net.lanet.vollmed.domain.syssql;

import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import net.lanet.vollmed.infra.utilities.exportfiles.TemplateGenericExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysSqlService implements ISysSqlService  {

    @Autowired
    private ApplicationProperties ap;
    @Autowired
    private TemplateGenericExport template;

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public SysSqlService(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public List<Map<String, Object>> executeSql(String sql, String resultSp) {

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return list;
    }

    @Override
    public List<Map<String, Object>> executeStoredProcedure(String spName, String resultSp, Map<String, Object> params) {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
//                .withSchemaName(ap.databaseName)
                .withCatalogName(ap.databaseName)
                .withProcedureName(spName);

        MapSqlParameterSource parameterSource;
        Map<String, Object> result = null;
        List<Map<String, Object>> resultList = null;
        String sql = null;

        switch (ap.databaseType.toLowerCase())
        {
            case "mysql":
                if (params != null) {
                    List<Object> paramValues = params.values().stream().collect(Collectors.toList());

                    result = jdbcCall.execute(paramValues.toArray());
                } else {
                    result = jdbcCall.execute();
                }

                // Converte o resultado para uma lista de mapas para retornar múltiplos registros
                resultList = (List<Map<String, Object>>) result.get("#result-set-1");
                break;

            case "sqlserver":
                List<Map<String, Object>> resultSqlServer = null;

                if (params != null) {
                    StringBuilder sqlBuilder = new StringBuilder("EXEC " + spName);
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        String paramName = entry.getKey();
                        Object paramValue = entry.getValue();
                        sqlBuilder.append(" ").append(paramName).append(" = ");
                        if (paramValue instanceof String) {
                            sqlBuilder.append("'").append(paramValue).append("'");
                        } else {
                            sqlBuilder.append(paramValue);
                        }
                        sqlBuilder.append(",");
                    }
                    sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
                    sqlBuilder.append(";");

                    sql = sqlBuilder.toString();
                } else {
                    sql = "EXEC " + spName;
                }

                resultSqlServer = jdbcTemplate.queryForList(sql);
                resultList = resultSqlServer;
                break;

            default:
                if (params != null) {
                    parameterSource = new MapSqlParameterSource(params);
                } else {
                    parameterSource = new MapSqlParameterSource();
                }
                result = jdbcCall.execute(parameterSource);

                // Converte o resultado para uma lista de mapas para retornar múltiplos registros
                resultList = (List<Map<String, Object>>) result.get("#result-set-1");
        }

        return resultList;
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
