package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessParameterGetRequest;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;

public interface UploadDocumentUseCase {
	UploadDocumentWithLog processDocument(UploadDocumentParameter uplDocPar, SqsMessObjUploadDoc obj);

	UploadDocumentApiResponse callRestClient(UploadDocumentParameter uplDocPar, String messageId);

	ProcessDocument getRequest(SqsMessParameterGetRequest sqspgr);
}
