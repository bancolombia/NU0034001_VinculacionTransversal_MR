package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;

public interface UploadDocumentValidateErrors {
	void validateExceptionRetries(
			Acquisition acquisition, String detail, SqsMessageParamAllObject sqsMessageParamAllObject);

	void validateExceptionRutRetries(
			Acquisition acquisition, String detail, String flag, SqsMessageParamAllObject sqsMessageParamAllObject);
}
