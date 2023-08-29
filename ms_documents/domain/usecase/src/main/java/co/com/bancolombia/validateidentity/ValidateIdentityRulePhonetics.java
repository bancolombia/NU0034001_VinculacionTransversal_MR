package co.com.bancolombia.validateidentity;

import org.apache.commons.text.similarity.LevenshteinDistance;

public interface ValidateIdentityRulePhonetics {
    LevenshteinDistance getDistance();

    boolean jaroWinklerDistance(String strOne, String strTwo, Double thresholdPhonetics);
}
