package net.lanet.vollmed.infra.utilities.exportfiles;

import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class GenerateDocumentCSV {

    public final static String TEXT_CSV = "text/csv";
    public final static String TEXT_TSV = "text/tab-separated-values";


    public void createDocument(HttpServletResponse response, String fileName, String extension) {
        // Document
        DateTimeUtil dtu = new DateTimeUtil();
        String currentDateTime = dtu.getNowFormatted(DateTimeUtil.formatter_FileExport);
        String headerKey = HttpHeaders.CONTENT_DISPOSITION;
        String headerValue = String.format("attachment;filename=%s_%s.%s",
                fileName, currentDateTime, (!extension.trim().equals("") ? extension.trim().toLowerCase() : ".csv") );
        response.setHeader(headerKey, headerValue);
        switch (extension) {
            case "csv" -> { response.setContentType(TEXT_CSV); }
            case "tsv" -> { response.setContentType(TEXT_TSV); }
            default -> { response.setContentType(TEXT_CSV); }
        }
    }

}
