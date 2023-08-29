package co.com.bancolombia.documentretries;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.documentretries.gateways.DocumentRetriesRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DocumentRetriesUseCaseImpl implements DocumentRetriesUseCase {

    private final DocumentRetriesRepository repository;
    private final CoreFunctionDate coreFunctionDate;

    @Override
    public DocumentRetries save(Acquisition acquisition) {
        Optional<DocumentRetries> documentRetries = findByAcquisition(acquisition);
        if (documentRetries.isPresent()) {
            DocumentRetries retries = documentRetries.get().toBuilder()
                    .uploadDocumentRetries(acquisition.getUploadDocumentRetries())
                    .uploadRutRetries(acquisition.getUploadRutRetries())
                    .updatedDate(coreFunctionDate.getDatetime())
                    .build();

            return repository.save(retries);
        }

        DocumentRetries retries = DocumentRetries.builder()
                .acquisition(acquisition)
                .uploadDocumentRetries(acquisition.getUploadDocumentRetries())
                .uploadRutRetries(acquisition.getUploadRutRetries())
                .createdDate(coreFunctionDate.getDatetime())
                .build();

        return repository.save(retries);
    }

    @Override
    public Optional<DocumentRetries> findByAcquisition(Acquisition acquisition) {
        return Optional.ofNullable(repository.findByAcquisition(acquisition));
    }
}
