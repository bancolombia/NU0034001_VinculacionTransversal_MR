package co.com.bancolombia.validateidentity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateIdentityRuleUtilUseCaseImpl implements ValidateIdentityRuleUtilUseCase {

	private final ValidateIdentityRulePhonetics validateIdentityRulePhonetics;

	@Override
	public boolean jaroWinklerDistance(String strOne, String strTwo, Double thresholdPhonetics) {
		return validateIdentityRulePhonetics.jaroWinklerDistance(strOne, strTwo, thresholdPhonetics);
	}

	@Override
	public boolean compareString(String strOne, String strTwo, Double thresholdPhonetics) {
		if (strOne.equals(strTwo)) {
			return true;
		} else {
			return jaroWinklerDistance(strOne, strTwo, thresholdPhonetics);
		}
	}
}
