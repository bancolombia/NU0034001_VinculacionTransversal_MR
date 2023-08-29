package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;

public interface UploadDocumentRutRulesUseCase {
    ProcessDocumentKofaxTotal validateRutDocument(
            UploadDocumentResponse uploadDocumentResponse, AcquisitionProcessDocument acquisition);
}
