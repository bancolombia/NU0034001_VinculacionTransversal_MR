package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;

public interface ProcessDocumentUseCase {
    UploadDocumentWithLog consumeKofax(ProcessDocument processDocument, String userTransaction) ;
}
