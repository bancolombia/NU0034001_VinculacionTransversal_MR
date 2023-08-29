package co.com.bancolombia.validateidentity;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOUBLE_ZERO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NUMBER_ONE_HUNDRED;

@RequiredArgsConstructor
public class ValidateIdentityRulePhoneticsImpl implements ValidateIdentityRulePhonetics {

	private final LevenshteinDistance distance = new LevenshteinDistance();

	@Override
	public LevenshteinDistance getDistance() {
		return distance;
	}

	@Override
	public boolean jaroWinklerDistance(String strOne, String strTwo, Double thresholdPhonetics) {
		JaroWinklerDistance algorithm = new JaroWinklerDistance();
		Double result;
		String[] partsStrOne = strOne.split(" ");
		String[] partsStrTwo = strTwo.split(" ");
		
		Double accumulate = DOUBLE_ZERO;
		if(partsStrOne.length == partsStrTwo.length && partsStrOne.length > 1) {
			for (int i = 0; i < partsStrOne.length; i++) {
				Double m = algorithm.apply(partsStrOne[i], partsStrTwo[i]);
				accumulate += m;
			}
			
			result = accumulate/partsStrOne.length;
		} else {
			result = algorithm.apply(strOne, strTwo);
		}

		return (result * NUMBER_ONE_HUNDRED) >= thresholdPhonetics;
	}
}
