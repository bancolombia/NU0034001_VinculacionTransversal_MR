package co.com.bancolombia.commonsvnt.usecase.util.constants.fortests;

import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex;

public class ConstantsRegexFor {

    public boolean validateConstantsRegex(String msg){
        return msg.equals(ConstantsRegex.REGEX_DOCUMENT_SUBTYPE_DIGITALIZATION);
    }
}
