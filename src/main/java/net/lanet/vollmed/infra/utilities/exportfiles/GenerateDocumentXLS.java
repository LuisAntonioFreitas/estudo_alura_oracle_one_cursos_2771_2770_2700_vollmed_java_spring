package net.lanet.vollmed.infra.utilities.exportfiles;


import jakarta.servlet.http.HttpServletResponse;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
public class GenerateDocumentXLS {

    private static final short COLOR_WHITE = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
    private static final short COLOR_BLACK = HSSFColor.HSSFColorPredefined.BLACK.getIndex();
    private static final short COLOR_DEFAULT_TITLE = HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex();
    private static final short COLOR_DEFAULT_FONT_TITLE = COLOR_WHITE;
    private static final short COLOR_DEFAULT_BORDER_TITLE = HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex();

    private static final short COLOR_DEFAULT_FILTER = HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex();
    private static final short COLOR_DEFAULT_FONT_FILTER = COLOR_BLACK;
    private static final short COLOR_DEFAULT_BORDER_FILTER = HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex();
    private static final short COLOR_DEFAULT_HEADER = COLOR_BLACK; //HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex();
    private static final short COLOR_DEFAULT_BORDER_HEADER = HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex(); // HSSFColor.HSSFColorPredefined.INDIGO.getIndex();
    private static final short COLOR_DEFAULT_FONT_HEADER = COLOR_WHITE;
    private static final short COLOR_DEFAULT_FONT = COLOR_BLACK;
    private static final short COLOR_DEFAULT_BORDER = COLOR_BLACK;
    private static final short COLOR_LINE_TOGGLE = HSSFColor.HSSFColorPredefined.LIGHT_TURQUOISE.getIndex();


    public void createDocument(HttpServletResponse response, String fileName) {
        // Document
        DateTimeUtil dtu = new DateTimeUtil();
        String currentDateTime = dtu.getNowFormatted(DateTimeUtil.formatter_FileExport);
        String headerKey = HttpHeaders.CONTENT_DISPOSITION;
        String headerValue = String.format("attachment;filename=%s_%s.xls",
                fileName, currentDateTime);
        response.setHeader(headerKey, headerValue);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public HSSFCellStyle styleDefaultTitle(HSSFWorkbook workbook) {
        // Style Default Title
        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(COLOR_DEFAULT_FONT_TITLE);
        font.setBold(true);
        font.setFontName("Calibri");
        style.setFont(font);

        style.setFillForegroundColor(COLOR_DEFAULT_TITLE);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THICK);
        style.setBottomBorderColor(COLOR_DEFAULT_BORDER_TITLE);

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public HSSFCellStyle styleDefaultFilter(HSSFWorkbook workbook) {
        // Style Default Filter
        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(COLOR_DEFAULT_FONT_FILTER);
        font.setBold(true);
        font.setFontName("Calibri");
        style.setFont(font);

        style.setFillForegroundColor(COLOR_DEFAULT_FILTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THICK);
        style.setBottomBorderColor(COLOR_DEFAULT_BORDER_FILTER);

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public HSSFCellStyle styleDefaultFooter(HSSFWorkbook workbook) {
        // Style Default Footer
        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 8);
        font.setColor(COLOR_DEFAULT_FONT_TITLE);
        font.setBold(true);
        font.setFontName("Calibri");
        style.setFont(font);

        style.setFillForegroundColor(COLOR_DEFAULT_TITLE);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderTop(BorderStyle.THICK);
        style.setTopBorderColor(COLOR_DEFAULT_BORDER_TITLE);

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public HSSFCellStyle styleDefaultHeader(HSSFWorkbook workbook) {
        // Style Default Header
        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(COLOR_DEFAULT_FONT_HEADER);
        font.setBold(true);
        font.setFontName("Calibri");
        style.setFont(font);

        style.setFillForegroundColor(COLOR_DEFAULT_HEADER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //styleBorder(style, BorderStyle.THICK, COLOR_DEFAULT_BORDER_HEADER);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBottomBorderColor(COLOR_DEFAULT_BORDER_HEADER);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setShrinkToFit(true);

        return style;
    }

    public HSSFCellStyle styleDefaultSubTotal(HSSFWorkbook workbook) {
        // Style Default SubTotal
        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(COLOR_DEFAULT_FONT_HEADER);
        font.setBold(true);
        font.setFontName("Calibri");
        style.setFont(font);

        style.setFillForegroundColor(COLOR_DEFAULT_HEADER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //styleBorder(style, BorderStyle.THICK, COLOR_DEFAULT_BORDER_HEADER);
        style.setBorderTop(BorderStyle.THICK);
        style.setTopBorderColor(COLOR_DEFAULT_BORDER_HEADER);

        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setShrinkToFit(true);

        return style;
    }

    public HSSFCellStyle styleDefaultCell(HSSFWorkbook workbook) {
        // Style Default Cell
        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(COLOR_DEFAULT_FONT);
        font.setFontName("Calibri");
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.TOP);

        return style;
    }
    public HSSFCellStyle styleDefaultCellToggle(HSSFWorkbook workbook, HSSFCellStyle styleOrigin) {
        // Style Default Cell Toggle
        HSSFCellStyle style = workbook.createCellStyle();

        style.cloneStyleFrom(styleOrigin);
        style.setFillForegroundColor(COLOR_LINE_TOGGLE);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    public void styleAlignment(HSSFCellStyle style,
                               HorizontalAlignment horizontal,
                               VerticalAlignment vertical) {
        style.setAlignment(horizontal);
        style.setVerticalAlignment(vertical);
    }

    public void styleBorder(HSSFCellStyle style, BorderStyle border, short color) {
        style.setBorderTop(border);
        style.setTopBorderColor(color);
        style.setBorderBottom(border);
        style.setBottomBorderColor(color);
        style.setBorderRight(border);
        style.setRightBorderColor(color);
        style.setBorderLeft(border);
        style.setLeftBorderColor(color);
    }

}

