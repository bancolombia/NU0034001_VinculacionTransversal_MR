package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;

public interface UploadDocumentCcRulesUseCase {
    ProcessDocumentKofaxTotal validateCcDocument(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition);
}
