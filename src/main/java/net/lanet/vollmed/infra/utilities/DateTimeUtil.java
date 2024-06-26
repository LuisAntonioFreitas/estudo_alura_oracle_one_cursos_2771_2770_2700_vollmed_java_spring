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

    public static final DateTimeFormatter formatterUTC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateTimeFormatter formatterDateLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatterTimeLocal = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final DateTimeFormatter formatter_ptBR = DateTimeFormatter.ofPattern("dd-MM-yyyy' 'HH:mm:ss");
    public static final DateTimeFormatter formatterDate_ptBR = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter formatterTime_ptBR = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final DateTimeFormatter formatter_enUS = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
    public static final DateTimeFormatter formatterDate_enUS = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatterTime_enUS = DateTimeFormatter.ofPattern("HH:mm:ss");


    public LocalDateTime getNowFormatUTC() {
        Instant instantOfNow = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instantOfNow, ZoneOffset.UTC);
        return localDateTime;
    }

    public LocalDateTime getNowFormatLocal() {
        Instant instantOfNow = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instantOfNow, ZoneOffset.UTC);
        LocalDateTime dateTime = localDateTime;

        if (ap.apiConfigLanguage.equalsIgnoreCase("pt_BR")) {
            localDateTime = LocalDateTime.ofInstant(instantOfNow, ZoneOffset.of("-03:00")); //Brasilia
            dateTime = LocalDateTime.parse(localDateTime.format(formatterLocal));
        }
        if (ap.apiConfigLanguage.equalsIgnoreCase("en_US")) {
            localDateTime = LocalDateTime.ofInstant(instantOfNow, ZoneOffset.of("-05:00")); //EST
            dateTime = LocalDateTime.parse(localDateTime.format(formatterLocal));
        }

        return dateTime;
    }

    public LocalDateTime convertFormatUTCtoLocal(LocalDateTime dateTimeFormatUTC) {
        Instant instant = dateTimeFormatUTC.toInstant(ZoneOffset.UTC);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDateTime dateTime = localDateTime;

        if (ap.apiConfigLanguage.equalsIgnoreCase("pt_BR")) {
            localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.of("-03:00")); //Brasilia
            dateTime = LocalDateTime.parse(localDateTime.format(formatterLocal));
        }
        if (ap.apiConfigLanguage.equalsIgnoreCase("en_US")) {
            localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.of("-05:00")); //EST
            dateTime = LocalDateTime.parse(localDateTime.format(formatterLocal));
        }

        return dateTime;
    }


    public String getFormatUTC(LocalDateTime dateTimeFormat) {
        return dateTimeFormat.format(formatterUTC);
    }

    public String getFormatLocal(LocalDateTime dateTimeFormat) {
        return dateTimeFormat.format(formatterLocal);
    }


    public String getFormat(LocalDateTime dateTimeFormat, String... language) {
        String value = language.length > 0 ? language[0] : ap.apiConfigLanguage;

        if (value.equalsIgnoreCase("pt_BR")) {
            return dateTimeFormat.format(formatter_ptBR);
        }
        if (value.equalsIgnoreCase("en_US")) {
            return dateTimeFormat.format(formatter_enUS);
        }

        return dateTimeFormat.format(formatterLocal);
    }

    public String getDateFormat(LocalDateTime dateTimeFormat, String... language) {
        String value = language.length > 0 ? language[0] : ap.apiConfigLanguage;

        if (value.equalsIgnoreCase("pt_BR")) {
            return dateTimeFormat.format(formatterDate_ptBR);
        }
        if (value.equalsIgnoreCase("en_US")) {
            return dateTimeFormat.format(formatterDate_enUS);
        }

        return dateTimeFormat.format(formatterDateLocal);
    }

    public String getTimeFormat(LocalDateTime dateTimeFormat, String... language) {
        String value = language.length > 0 ? language[0] : ap.apiConfigLanguage;

        if (value.equalsIgnoreCase("pt_BR")) {
            return dateTimeFormat.format(formatterTime_ptBR);
        }
        if (value.equalsIgnoreCase("en_US")) {
            return dateTimeFormat.format(formatterTime_enUS);
        }

        return dateTimeFormat.format(formatterTimeLocal);
    }

}
