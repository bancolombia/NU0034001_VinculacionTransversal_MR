package co.com.bancolombia.api.validatelegalrep;

import co.com.bancolombia.api.model.validatelegalrep.ValidateLegalRepResponse;
import co.com.bancolombia.api.model.validatelegalrep.ValidateLegalRepResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.model.validatelegalrep.ValidateLegalRep;

public class VLRResponseFactory {

	private VLRResponseFactory() {
	}

	public static ValidateLegalRepResponse buildValidateLegalRepResponse(
			UserInfoRequest request, ValidateLegalRep validateLegalRep) {
		MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());

		ValidateLegalRepResponseData dataResponse = ValidateLegalRepResponseData.builder()
				.validationCode(validateLegalRep.getValidationCode())
				.validationDescription(validateLegalRep.getValidationDescription()).build();
		return ValidateLegalRepResponse.builder().meta(metaResponse).data(dataResponse).build();
	}
}