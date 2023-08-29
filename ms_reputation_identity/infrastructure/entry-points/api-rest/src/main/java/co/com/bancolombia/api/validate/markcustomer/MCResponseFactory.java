package co.com.bancolombia.api.validate.markcustomer;

import co.com.bancolombia.api.model.markcustomer.MarkCustomerResponse;
import co.com.bancolombia.api.model.markcustomer.MarkCustomerResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;

public class MCResponseFactory {

	private MCResponseFactory() {
	}

	public static MarkCustomerResponse buildMarkCustomerResponse(
			UserInfoRequest request, MarkCustomerResponseData markCustomer) {
		MarkCustomerResponseData responseData = MarkCustomerResponseData.builder()
				.answerCode(markCustomer.getAnswerCode()).answerName(markCustomer.getAnswerName()).build();
		MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
		return MarkCustomerResponse.builder().data(responseData).meta(metaResponse).build();
	}
}