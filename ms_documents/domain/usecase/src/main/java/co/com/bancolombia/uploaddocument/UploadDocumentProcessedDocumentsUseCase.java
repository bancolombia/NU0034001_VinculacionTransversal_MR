package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UploadDocumentProcessedDocumentsUseCase {
    void saveProcessedDocuments(
            UploadDocumentParameter uploadDocumentParameter, Date responseDate,
            SqsMessageParamAllObject sqsMessageParamAllObject, Map<String, String> values);

    void saveProcessedDocuments(SqsMessageParamAllObject sqsMessageParamAllObject);

    boolean validateAsynchronousProcess(List<UploadDocumentFile> listGet);

    void moveFileDocumentsBucket(List<String> fileNames);

}
