package net.lanet.vollmed.infra.utilities.exportfiles;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public class HandleExportFile {
    public static <T extends IHandleExportFile> void execute (
            String type, T service, HttpServletResponse response,
            List<Map<String, Object>> list, String fileName, String title, String filter, String tabName) {
        try {
            switch (type) {
                case "xls" -> { service.generateXLS(response, list, fileName, title, filter, tabName); }
                case "csv" -> { service.generateCSV(response, list, fileName); }
                case "tsv" -> { service.generateTSV(response, list, fileName); }
                case "pdf" -> { service.generatePDF(response, list, fileName); }
            }
        } catch (Exception ignored) {}
    }
}
