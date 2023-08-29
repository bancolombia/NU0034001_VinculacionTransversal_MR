package co.com.bancolombia.commonsvnt.usecase.util.constants;

import lombok.Data;

@Data
public class ConstantsRegex {

    /**
     * Regular Expressions
     */
    public static final String REGEX_ZERO = "^(0)$";

    public static final String REGEX_ONE_OR_TWO = "^(1|2)$";

    public static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}" +
            "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    public static final String REGEX_NUMBERS_TAM_TWENTY = "^\\d{1,20}$";

    public static final String REGEX_NUMBERS_TAM_TWO = "^\\d{1,2}$";

    public static final String REGEX_DECIMAL = "^\\d+(\\.[0-9][0-9])?$";

    public static final String REGEX_REGISTRATION_DATE = "^\\d{4}(0[1-9]|1[012])([0-2][0-9]|3[01])([01][0-9]|2[0-3])" +
            "([0-5][0-9])([0-5][0-9])";

    public static final String REGEX_DATE_FORMAT = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    public static final String REGEX_ALPHANUMERIC_WITHOUT_SPECIALS = "^[a-zA-Z0-9]+$";

    public static final String REGEX_LETTERS = "^[a-zA-Z\\u00f1\\u00d1]*$";

    public static final String REGEX_LETTERS_WITH_SPACES = "^[a-zA-Z\\u00f1\\u00d1\\s]{2,}$";

    public static final String REGEX_LETTERS_SPACES = "^[a-zA-Z\\u00f1\\u00d1\\s]*$";

    public static final String REGEX_ALPHANUMERIC = "^[A-Za-z0-9\\u00f1\\u00d1\\s]+$";
    
    public static final String REGEX_ALPHANUMERIC_UPLOAD_DOCUMENT = "(.*?)\\.(jpg|jpeg|pdf|png|tif|tiff)$";

    public static final String REGEX_EMPLOYEES_NUMBER = "^\\d{1,7}$";

    public static final String REGEX_EMAIL = "^[a-zA-z0-9-.]+@[a-zA-z0-9-]+\\.[a-zA-Z]+\\.?[a-zA-Z]+$";

    public static final String REGEX_EXT = "^[0-9]*$";

    public static final String REGEX_CELLPHONE = "^(?!.*(\\d).*\\1{9,})\\d{0,30}$";

    public static final String REGEX_PHONE = "^(?!.*(\\d).*\\1{6,})\\d{0,30}$";

    public static final String REGEX_NEIGHBORHOOD = "^[a-zA-Z0-9\\s#();,.\"\\u00f1\\u00d1]" +
            "[a-zA-Z0-9\\s\\-#/();,.\"\\u00f1\\u00d1]*$";

    public static final String REGEX_ADDRESS = "^[a-zA-Z0-9\\s#.,();´'_\"\\u00f1\\u00d1]" +
            "[a-zA-Z0-9\\s\\-#.,/();´'_\"\\u00f1\\u00d1]*$";

    public static final String REGEX_COMPANY_NAME = "^[a-zA-ZÀ-ÿñÑ0-9_\\\\s\\\\#&(): ;,.\\\"°_´']" +
            "[a-zA-ZÀ-ÿñÑ0-9_\\-\\\\s\\\\#&(): ;+,.\\\"°_´']*[a-zA-ZÀ-ÿ0-9_\\-\\\\s\\\\#&(): ;+,.\\\"°_´']$";

    public static final String REGEX_NAME_ENTITY = "^[a-zA-Z0-9\\s\\[#&():;%!¿?¡\\],\"°_'\\u00f1\\u00d1]" +
            "[a-zA-Z0-9\\s\\-#&():;+%!¿?¡\\[\\]=,.\"/°_'\\u00f1\\u00d1]*$";

    public static final String REGEX_PRODUCT_NUMBER = "^[A-Za-z0-9\\u00f1\\u00d1\\s]" +
            "[A-Za-z0-9\\u00f1\\u00d1\\s\\-]*$";

    public static final String REGEX_INJECTION_HTML = "^(.*(<|&lt;|&lt)\\s*.*(>|&gt|&gt;))|" +
            "(.*(<|&lt;|&lt).*\\s*(>|&gt|&gt;))$";

    public static final String REGEX_DOCUMENT_SUBTYPE_DIGITALIZATION = "^(001|002)$";

    public static final String REGEX_DOCUMENT_TYPE_DIGITALIZATION = "^(01)$";

    public static final String REGEX_PERSISTENCE_DOCUMENT_CODE = "^(000|001|002)$";

    public static final String REGEX_EMPTY_ACCENT_MARK = "[\\p{InCombiningDiacriticalMarks}]";

    private ConstantsRegex() {
    }
}
