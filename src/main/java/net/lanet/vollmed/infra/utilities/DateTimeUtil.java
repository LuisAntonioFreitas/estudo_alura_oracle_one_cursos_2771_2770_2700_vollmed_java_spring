package net.lanet.vollmed.infra.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil {
    @Autowired
    private ApplicationProperties ap;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateTimeFormatter formatter_ptBR = DateTimeFormatter.ofPattern("dd-MM-yyyy' 'HH:mm:ss");
    public static final DateTimeFormatter formatter_ptBR_Date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter formatter_ptBR_Time = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter formatter_enUS = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
    public static final DateTimeFormatter formatter_enUS_Date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatter_enUS_Time = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LocalDateTime getNow() {
        // Ex.: 2024-06-30T18:57:27.471135
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
    public LocalDateTime getNowUTC() {
        // Ex.: 2024-06-30T21:57:27.471135
        Instant instantOfNow = Instant.now();
        LocalDateTime now = LocalDateTime.ofInstant(instantOfNow, ZoneOffset.UTC);
        return now;
    }
    public String getNowFormatted(String... format) {
        String value = format.length > 0 ? format[0] : "";

        String now = formatted(getNow(), value);
        return now;
    }
    public String getNowFormatted(DateTimeFormatter format) {
        String now = formatted(getNow(), format);
        return now;
    }
    public String getNowUTCFormatted(String... format) {
        String value = format.length > 0 ? format[0] : "";

        String now = formatted(getNowUTC(), value);
        return now;
    }
    public String getNowUTCFormatted(DateTimeFormatter format) {
        String now = formatted(getNowUTC(), format);
        return now;
    }
    public String formattedDateTime(LocalDateTime dt, String... format) {
        String value = format.length > 0 ? format[0] : "";

        String newFormat = formatted(dt, value);
        return newFormat;
    }
    public String formattedDateTime(LocalDateTime dt, DateTimeFormatter format) {
        String newFormat = formatted(dt, format);
        return newFormat;
    }
    private String formatted(LocalDateTime ltd, String... format) {
        String value = format.length > 0 ? format[0] : "";

        DateTimeFormatter f = formatter; // Ex.: 2024-06-30T19:49:45
        if (!value.isEmpty()) {
            f = DateTimeFormatter.ofPattern(value); // Ex.: ?
        } else {
            // Ex.: 30-06-2024 19:49:45
            if (ap.apiConfigLanguage.equalsIgnoreCase("pt_BR")) { f = formatter_ptBR; }
            // Ex.: 2024-06-30 19:49:45
            if (ap.apiConfigLanguage.equalsIgnoreCase("en_US")) { f = formatter_enUS; }
        }
        String newFormat = ltd.format(f);
        return newFormat;
    }
    private String formatted(LocalDateTime ltd, DateTimeFormatter format) {
        DateTimeFormatter f = formatter; // Ex.: 2024-06-30T19:49:45
        if (format != null) {
            f = format; // Ex.: ?
        } else {
            // Ex.: 30-06-2024 19:49:45
            if (ap.apiConfigLanguage.equalsIgnoreCase("pt_BR")) { f = formatter_ptBR; }
            // Ex.: 2024-06-30 19:49:45
            if (ap.apiConfigLanguage.equalsIgnoreCase("en_US")) { f = formatter_enUS; }
        }
        String newFormat = ltd.format(f);
        return newFormat;
    }
    public LocalDateTime convertUTCtoLTD(LocalDateTime ldt) {
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        LocalDateTime convert = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        if (ap.apiConfigLanguage.equalsIgnoreCase("pt_BR")) {
            convert = LocalDateTime.ofInstant(instant, ZoneOffset.of("-03:00")); //Brasilia
            convert = LocalDateTime.parse(convert.format(formatter));
        }
        if (ap.apiConfigLanguage.equalsIgnoreCase("en_US")) {
            convert = LocalDateTime.ofInstant(instant, ZoneOffset.of("-05:00")); //EST
            convert = LocalDateTime.parse(convert.format(formatter));
        }
        return convert;
    }

}
