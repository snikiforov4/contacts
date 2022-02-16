package contacts.client;

import java.util.regex.Pattern;

public final class ParsingUtils {

    private static final Pattern DIGITS = Pattern.compile("\\d+");

    private ParsingUtils() {
    }

    public static boolean isNumber(String text) {
        return text != null && DIGITS.matcher(text).matches();
    }
}
