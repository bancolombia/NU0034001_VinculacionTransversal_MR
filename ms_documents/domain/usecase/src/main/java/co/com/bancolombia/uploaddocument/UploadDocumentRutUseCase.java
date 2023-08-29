package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;

public interface UploadDocumentRutUseCase {
    UploadDocumentWithLog processDocument(UploadDocumentParameter uploadDocumentParameter, SqsMessObjUploadDoc obj);
}
