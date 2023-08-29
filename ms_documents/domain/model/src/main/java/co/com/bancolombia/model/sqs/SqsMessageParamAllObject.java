package co.com.bancolombia.model.sqs;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsMessageParamAllObject {
	private Acquisition acquisition;
	private UploadDocumentApiResponse uploadDocumentApiResponse;
	private SqsMessObjUploadDoc sqsMessObjUploadDoc;
	private SqsMessage sqsMessage;
	private UploadDocumentParameter uploadDocumentParameter;
}
