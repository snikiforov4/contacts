package contacts.service;

import java.util.regex.Pattern;

public class PhoneNumberValidator {

    private static final Pattern FIRST_TOKEN = Pattern.compile("\\+?(\\d|[0-9a-zA-Z]{2,}|\\([0-9a-zA-Z]{2,}\\))");
    private static final Pattern LAST_TOKEN = Pattern.compile("([0-9a-zA-Z]{2,}|\\([0-9a-zA-Z]{2,}\\))");

    public boolean isValid(String number) {
        String[] tokens = number.split("[ -]");
        if (tokens.length == 0) {
            return false;
        }
        boolean onePairOfRoundBrackets = false;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.isEmpty()) {
                return false;
            }
            if (i == 0) {
                if (!FIRST_TOKEN.matcher(token).matches()) {
                    return false;
                }
            } else {
                if (!LAST_TOKEN.matcher(token).matches()) {
                    return false;
                }
            }
            if (token.contains("(")) {
                if (onePairOfRoundBrackets) {
                    return false;
                }
                onePairOfRoundBrackets = true;
            }
        }
        return true;
    }

}
