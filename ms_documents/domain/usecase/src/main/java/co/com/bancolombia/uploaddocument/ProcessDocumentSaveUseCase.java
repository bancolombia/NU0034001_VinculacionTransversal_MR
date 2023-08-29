package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.DigitalizationIdentitySave;
import co.com.bancolombia.model.uploaddocument.DigitalizationRutSave;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;

import java.util.Optional;
import java.util.UUID;

public interface ProcessDocumentSaveUseCase {
    void saveDigitalizationIdentity(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction);

    DigitalizationIdentitySave transDigitalizationIdentitySave(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction);

    void saveDigitalizationRut(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction);

    DigitalizationRutSave transDigitalizationRutSave(UploadDocumentResponse uploadDocumentResponse,
            AcquisitionProcessDocument acquisition, String userTransaction);

    Optional<DigitalizationRutSave> findByAcquisition(UUID acquisitionId);
}
