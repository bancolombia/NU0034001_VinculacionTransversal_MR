package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;

public interface UploadDocumentExcepUseCase {
	void validateBackEndException(
			UploadDocumentApiResponse uploadDocumentApiResponse, SqsMessageParamAllObject sqsMessageParamAllObject);

	void validateException(
			String exception, String detail, String complement, SqsMessageParamAllObject sqsMessageParamAllObject);

	void validateBusinessException(
			ProcessDocumentKofaxError processDocumentKofaxError, Acquisition acquisition,
			SqsMessageParamAllObject sqsMessageParamAllObject);
}
