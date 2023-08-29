package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AcquisitionUseCaseImpl implements AcquisitionUseCase {

    private final DocumentRetriesUseCase documentRetriesUseCase;

    @Override
    public Acquisition getAcquisition(SqsMessObjUploadDoc obj) {
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(obj.getData().getAcquisition().getId()))
                .documentNumber(obj.getData().getAcquisition().getDocumentNumber())
                .documentType(DocumentType.builder().code(obj.getData().getAcquisition().getDocumentType()).build())
                .uploadDocumentRetries(0).uploadRutRetries(0).build();

        Optional<DocumentRetries> documentRetries = documentRetriesUseCase.findByAcquisition(acquisition);
        if (documentRetries.isPresent()) {
            acquisition.setUploadRutRetries(documentRetries.get().getUploadRutRetries());
            acquisition.setUploadDocumentRetries(documentRetries.get().getUploadDocumentRetries());
        }

        return acquisition;
    }
}
