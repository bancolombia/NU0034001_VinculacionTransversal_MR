package co.com.bancolombia.validateidentity;

public interface ValidateIdentityRuleUtilUseCase {
    boolean jaroWinklerDistance(String strOne, String strTwo, Double thresholdPhonetics);

    boolean compareString(String strOne, String strTwo, Double thresholdPhonetics);
}
