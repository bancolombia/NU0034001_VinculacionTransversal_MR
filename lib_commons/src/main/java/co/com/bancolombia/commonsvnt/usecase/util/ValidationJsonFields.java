package co.com.bancolombia.commonsvnt.usecase.util;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_INJECTION_HTML;

public class ValidationJsonFields {

    private ValidationJsonFields() {
    }

    public static boolean validationEmail(String email) {
        if (email != null && email.matches(REGEX_EMAIL)) {
            email = email.toLowerCase();
            String[] firstPartition = email.split("@");
            String[] secondPartition = firstPartition[1].split("\\.");
            boolean firstValidation = Constants.STRINGS_EMAIL_ERROR.contains(firstPartition[0]);
            boolean secondValidation = false;
            for (String string : secondPartition) {
                secondValidation = Constants.STRINGS_EMAIL_ERROR.contains(string) || secondValidation;
            }
            if (firstValidation || secondValidation) {
                return false;
            }
        }
        return true;
    }

    public static boolean validationInjectionHTML(String string) {
        return string == null || !string.matches(REGEX_INJECTION_HTML);
    }
}
