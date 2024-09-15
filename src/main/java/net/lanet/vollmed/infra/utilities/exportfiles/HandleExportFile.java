package net.lanet.vollmed.infra.utilities.exportfiles;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HandleExportFile implements IHandleExportFile {
    @Autowired
    private TemplateGenericExport template;

    public void execute (
            String type, HttpServletResponse response,
            List<Map<String, Object>> list, String fileName, String title, String filter, String tabName) {
        try {
            switch (type) {
                case "xls" -> { this.generateXLS(response, list, fileName, title, filter, tabName); }
                case "csv" -> { this.generateCSV(response, list, fileName); }
                case "tsv" -> { this.generateTSV(response, list, fileName); }
                case "pdf" -> { this.generatePDF(response, list, fileName); }
            }
        } catch (Exception ignored) {}
    }

    public void generateXLS(HttpServletResponse response, List<Map<String, Object>> list, String fileName,
                            String title, String filter, String tabName) {
        // Excel
        template.generateXLS(response, list, fileName, title, filter, tabName);
    }
    public void generateCSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        template.generateCSV(response, list, fileName);
    }
    public void generateTSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        template.generateTSV(response, list, fileName);
    }
    public void generatePDF(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        template.generatePDF(response, list, fileName);
    }
}
