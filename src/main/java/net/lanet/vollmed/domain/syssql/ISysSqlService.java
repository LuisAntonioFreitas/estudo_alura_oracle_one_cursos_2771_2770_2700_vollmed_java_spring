package net.lanet.vollmed.domain.syssql;

import java.util.List;
import java.util.Map;

public interface ISysSqlService {
    List<Map<String, Object>> executeSql(String sql, String resultSp);
    List<Map<String, Object>> executeStoredProcedure(String spName, String resultSp, Map<String, Object> params);
}
