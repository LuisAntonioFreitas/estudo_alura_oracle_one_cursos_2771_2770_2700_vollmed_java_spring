package net.lanet.vollmed.infra.utilities;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Component
public class RegexUtil {
    public static String normalizeStringLettersAndNumbers(String input) {
        // Normalize the string to decompose accented characters
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Use regex to remove diacritics (accents) and special characters
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("");

        // Remove all non-alphanumeric characters
        String regex = "[^a-zA-Z0-9]+";
        result = result.replaceAll(regex, "");

        return result;
    }
}
