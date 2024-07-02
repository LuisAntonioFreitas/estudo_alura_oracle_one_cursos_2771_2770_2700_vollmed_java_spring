package net.lanet.vollmed.infra.utilities.exportfiles;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public interface IHandleExportFile {
    void generateXLS(HttpServletResponse response, List<Map<String, Object>> list, String fileName,
                     String title, String filter, String tabName);
    void generateCSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName);
    void generateTSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName);
    void generatePDF(HttpServletResponse response, List<Map<String, Object>> list, String fileName);
}
