package net.lanet.vollmed.infra.utilities.exportfiles;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import net.lanet.vollmed.infra.utilities.MessageConsoleUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class TemplateGenericExport {

    @Autowired
    private ApplicationProperties ap;
    @Autowired
    private DateTimeUtil dtu;
    @Autowired
    private MessageConsoleUtil messageConsoleUtil;


    public void generateXLS(HttpServletResponse response, List<Map<String, Object>> list, String fileName,
                            String title, String filter, String tabName) {

        // Excel
        GenerateDocumentXLS fileGenerate = new GenerateDocumentXLS();
        fileGenerate.createDocument(response, fileName);

        // Document
        try (
                HSSFWorkbook workbook = new HSSFWorkbook();
                ServletOutputStream ops = response.getOutputStream()
        ) {
            // Tab
            HSSFSheet sheet1 = workbook.createSheet(tabName.toLowerCase());

            // Formato de Datas
            DateTimeFormatter formatter = dtu.formattedSystemDefault();

            // Definindo Colunas e Preparando Linhas
            if (list != null && !list.isEmpty()) {
                // Obtém as chaves para definir as colunas
                Set<String> columns = list.get(0).keySet();
                List<String> columnList = new ArrayList<>(columns);
//                System.out.println("Colunas: " + columns);

                // Obtém os valores para preencher as linhas
                List<List<Object>> rows = list.stream()
                        .map(item -> columns.stream().map(item::get).collect(Collectors.toList()))
                        .collect(Collectors.toList());
                List<List<Object>> rowList = new ArrayList<>(rows);
//                System.out.println("Linhas: " + rows);

                // Title
                int totalCols = columns.size();
                int dataRowIndex = 0;

                // Style Default Title
                HSSFRow titleRow = sheet1.createRow(dataRowIndex);
                titleRow.setHeight((short) 450);
                HSSFCellStyle titleStyle = fileGenerate.styleDefaultTitle(workbook);
                for (int col = 0; col < (totalCols); col++) {
                    titleRow.createCell(col);
                    titleRow.getCell(col).setCellStyle(titleStyle);
                }

                // Style Default Filter
                HSSFRow filterRow = null;
                if (filter != null) {
                    dataRowIndex++;
                    filterRow = sheet1.createRow(dataRowIndex);
                    filterRow.setHeight((short) 450);
                    HSSFCellStyle filterStyle = fileGenerate.styleDefaultFilter(workbook);
                    for (int col = 0; col < (totalCols); col++) {
                        filterRow.createCell(col);
                        filterRow.getCell(col).setCellStyle(filterStyle);
                    }
                }

                // Header
                dataRowIndex++;
                HSSFRow headerRow = sheet1.createRow(dataRowIndex);
                for (int col = 0; col < (totalCols); col++) {
                    String column = columnList.get(col);
                    headerRow.createCell(col).setCellValue(column.trim().toUpperCase());
                }

                // Style Default Header
                HSSFCellStyle headerStyle = fileGenerate.styleDefaultHeader(workbook);
                for (int col = 0; col < (totalCols); col++) {
                    headerRow.getCell(col).setCellValue("  " + headerRow.getCell(col).getStringCellValue() + "      ");
                    headerRow.getCell(col).setCellStyle(headerStyle);

                    sheet1.autoSizeColumn(col);
                    int colSize = sheet1.getColumnWidth(col) + 800;
                    sheet1.setColumnWidth(col, colSize);
//                  sheet1.setColumnWidth(col, 200);
                }

                // Auto Filter and Freeze Pane
                CellRangeAddress range = new CellRangeAddress(dataRowIndex, dataRowIndex, 0, totalCols - 1);
                sheet1.setAutoFilter(range);
                sheet1.createFreezePane(0, dataRowIndex + 1);

                // Rows
                HSSFCellStyle cellStyleDefault = fileGenerate.styleDefaultCell(workbook);
                HSSFCellStyle cellStyleToggle = fileGenerate.styleDefaultCellToggle(workbook, cellStyleDefault);

                HSSFCellStyle cellStyleDefaultAlignmentCenter = workbook.createCellStyle();
                cellStyleDefaultAlignmentCenter.cloneStyleFrom(cellStyleDefault);
                fileGenerate.styleAlignment(cellStyleDefaultAlignmentCenter, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
                HSSFCellStyle cellStyleToggleAlignmentCenter = workbook.createCellStyle();
                cellStyleToggleAlignmentCenter.cloneStyleFrom(cellStyleToggle);
                fileGenerate.styleAlignment(cellStyleToggleAlignmentCenter, HorizontalAlignment.CENTER, VerticalAlignment.TOP);

                HSSFCellStyle cellStyleDefaultAlignmentRight = workbook.createCellStyle();
                cellStyleDefaultAlignmentRight.cloneStyleFrom(cellStyleDefault);
                fileGenerate.styleAlignment(cellStyleDefaultAlignmentRight, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);
                HSSFCellStyle cellStyleToggleAlignmentRight = workbook.createCellStyle();
                cellStyleToggleAlignmentRight.cloneStyleFrom(cellStyleToggle);
                fileGenerate.styleAlignment(cellStyleToggleAlignmentRight, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);

                dataRowIndex++;
                for (int i = 0; i < rowList.size(); i++) {
                    List<Object> row = rowList.get(i);
                    HSSFRow dataRow = sheet1.createRow(dataRowIndex);
                    for (int e = 0; e < row.size(); e++) {
                        Object item = row.get(e);
                        if (item instanceof String) {
//                            dataRow.createCell(e).setCellValue((String) item);
//                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
//                                    ? cellStyleDefault : cellStyleToggle);

                            try {
                                // Testa se o dado é uma data antes de adicionar
                                String cleanItem = item.toString().replace("T", " ").replace("Z",".0");
                                Timestamp timestampItem = Timestamp.valueOf(cleanItem);
                                Object date = ((Timestamp) timestampItem).toLocalDateTime();
                                Object result = ((LocalDateTime) date).format(formatter);
                                dataRow.createCell(e).setCellValue((String) result);
                                dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                        ? cellStyleDefaultAlignmentCenter : cellStyleToggleAlignmentCenter);
                            } catch (Exception ex) {
                                dataRow.createCell(e).setCellValue((String) item);
                                dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                        ? cellStyleDefault : cellStyleToggle);
                            }

                        } else if (item instanceof Boolean) {
                            dataRow.createCell(e).setCellValue((Boolean) item);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentCenter : cellStyleToggleAlignmentCenter);
                        } else if (item instanceof Integer) {
                            dataRow.createCell(e).setCellValue((Integer) item);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentRight : cellStyleToggleAlignmentRight);
                        } else if (item instanceof Long) {
                            dataRow.createCell(e).setCellValue((Long) item);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentRight : cellStyleToggleAlignmentRight);
                        } else if (item instanceof Double) {
                            dataRow.createCell(e).setCellValue((Double) item);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentRight : cellStyleToggleAlignmentRight);
                        } else if (item instanceof Float) {
                            dataRow.createCell(e).setCellValue((Float) item);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentRight : cellStyleToggleAlignmentRight);
                        } else if (item instanceof BigDecimal) {
                            float result = ((BigDecimal) item).floatValue();

                            dataRow.createCell(e).setCellValue((Float) result);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentRight : cellStyleToggleAlignmentRight);
                        } else if (item instanceof Short) {
                            dataRow.createCell(e).setCellValue((Short) item);
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentRight : cellStyleToggleAlignmentRight);
                        } else if (item instanceof Timestamp) {
                            try {
                                // Date and Time
                                Object date = ((Timestamp) item).toLocalDateTime();
                                Object result = ((LocalDateTime) date).format(formatter);

                                dataRow.createCell(e).setCellValue((String) result);
                            } catch (Exception ex) {
                                dataRow.createCell(e).setCellValue((String) item);
                            }
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentCenter : cellStyleToggleAlignmentCenter);
                        } else if (item instanceof LocalDateTime) {
                            try {
                                // Date and Time
                                Object date = ((LocalDateTime) item);
                                Object result = ((LocalDateTime) date).format(formatter);

                                dataRow.createCell(e).setCellValue((String) result);
                            } catch (Exception ex) {
                                dataRow.createCell(e).setCellValue((String) item);
                            }
                            dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                    ? cellStyleDefaultAlignmentCenter : cellStyleToggleAlignmentCenter);
                        } else {
                            try {
                                dataRow.createCell(e).setCellValue((String) item);
                                dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                        ? cellStyleDefault : cellStyleToggle);
                            } catch (Exception ex) {
                                if (item.getClass().isEnum()) {
                                    dataRow.createCell(e).setCellValue(item.toString());
                                    dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                            ? cellStyleDefault : cellStyleToggle);
                                } else {
                                    StringBuilder resultException = new StringBuilder();
                                    Class<?> objClass = item.getClass();
                                    Field[] fields = objClass.getDeclaredFields();
                                    Arrays.stream(fields).forEach(field -> {
                                        field.setAccessible(true);
                                        try {
                                            String titleException = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                            var valueException = field.get(item);
                                            resultException.append(String.format("%s: %s\n", titleException, valueException));
                                        } catch (Exception ignored) {}
                                    });
                                    if (!resultException.isEmpty()) {
                                        String resultFinalException = resultException.substring(0, (resultException.length()-1));
                                        dataRow.createCell(e).setCellValue((String) resultFinalException.trim());
                                    }
                                    dataRow.getCell(e).setCellStyle((dataRowIndex % 2) != 0
                                            ? cellStyleDefault : cellStyleToggle);
                                }
                            }
                        }
                        sheet1.autoSizeColumn(e);
                        int colSize = sheet1.getColumnWidth(e) + 800;
                        sheet1.setColumnWidth(e, colSize);
                    }
                    dataRowIndex++;
                }

                // Define Title
                titleRow.getCell(0).setCellValue((String) String.format(title));

                // Define Filter
                if (filter != null) {
                    String defineFilter = String.format("Filtro(s)  =  %s", filter);
                    filterRow.getCell(0).setCellValue((String) String.format(defineFilter));
                }

                // Style Default SubTotal
                HSSFRow subTotalRow = sheet1.createRow(dataRowIndex);
                HSSFCellStyle subTotalStyle = fileGenerate.styleDefaultSubTotal(workbook);
                for (int col = 0; col < (totalCols); col++) {
                    subTotalRow.createCell(col);
                    subTotalRow.getCell(col).setCellStyle(subTotalStyle);
                }

                // Style Default Footer
                dataRowIndex++;
                HSSFRow foterRow = sheet1.createRow(dataRowIndex);
                foterRow.setHeight((short) 325);
                HSSFCellStyle footerStyle = fileGenerate.styleDefaultFooter(workbook);
                for (int col = 0; col < (totalCols); col++) {
                    foterRow.createCell(col);
                    foterRow.getCell(col).setCellStyle(footerStyle);
                }
                // Define Footer
                foterRow.getCell(0).setCellValue((String)
                        String.format("%s | %s", ap.apiSystemName, LocalDateTime.now().format(dtu.formattedFooterSystemDefault())));

            }

            // Salve
            workbook.write(ops);
//            workbook.close();
//            ops.close();
            messageConsoleUtil.showMessage(String.format("Export file XLS, %s.xls",
                    response.getHeader("Content-Disposition")
                            .replace("attachment;filename=", "")
                            .replace(".xls", "")));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public void generateCSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {

        // CSV
        GenerateDocumentCSV fileGenerate = new GenerateDocumentCSV();
        fileGenerate.createDocument(response, fileName, "csv");

        char separador = ';';
        try (
                ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(), new CsvPreference
                        .Builder('"', separador, "\n")
                        .useQuoteMode(new AlwaysQuoteMode()).build());
        ) {
            if (list != null && !list.isEmpty()) {
                Set<String> columns = list.get(0).keySet();
                String[] columnList = columns.toArray(new String[0]);

                List<List<Object>> rows = list.stream()
                        .map(item -> columns.stream().map(item::get).collect(Collectors.toList()))
                        .collect(Collectors.toList());
                List<List<Object>> rowList = new ArrayList<>(rows);

                // Header
                csvWriter.writeHeader(columnList);
                // Rows
                for (List<Object> row : rowList) {
                    csvWriter.write(row);
                }
                // Salve
                csvWriter.close();
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        messageConsoleUtil.showMessage(String.format("Export file CSV, %s.csv",
                response.getHeader("Content-Disposition")
                        .replace("attachment;filename=", "")
                        .replace(".csv", "")));
    }


    public void generateTSV(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {

        // TSV
        GenerateDocumentCSV fileGenerate = new GenerateDocumentCSV();
        fileGenerate.createDocument(response, fileName, "tsv");

        char separador = '\t';
        try (
                ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(), new CsvPreference
                        .Builder('"', separador, "\n")
                        .useQuoteMode(new AlwaysQuoteMode()).build());
        ) {
            if (list != null && !list.isEmpty()) {
                Set<String> columns = list.get(0).keySet();
                String[] columnList = columns.toArray(new String[0]);

                List<List<Object>> rows = list.stream()
                        .map(item -> columns.stream().map(item::get).collect(Collectors.toList()))
                        .collect(Collectors.toList());
                List<List<Object>> rowList = new ArrayList<>(rows);

                // Header
                csvWriter.writeHeader(columnList);
                // Rows
                for (List<Object> row : rowList) {
                    csvWriter.write(row);
                }
                // Salve
                csvWriter.close();
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        messageConsoleUtil.showMessage(String.format("Export file TSV, %s.tsv",
                response.getHeader("Content-Disposition")
                        .replace("attachment;filename=", "")
                        .replace(".tsv", "")));
    }


    public void generatePDF(HttpServletResponse response, List<Map<String, Object>> list, String fileName) {
        messageConsoleUtil.showMessage(String.format("Wait in production: Export file PDF, %s.pdf", fileName));
    }

}
