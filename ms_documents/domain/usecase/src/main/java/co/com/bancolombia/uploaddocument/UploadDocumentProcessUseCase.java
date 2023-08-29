package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;

public interface UploadDocumentProcessUseCase {
    UploadDocumentApiResponse processDocument(ProcessDocument processDocument, String userTransaction);
}
