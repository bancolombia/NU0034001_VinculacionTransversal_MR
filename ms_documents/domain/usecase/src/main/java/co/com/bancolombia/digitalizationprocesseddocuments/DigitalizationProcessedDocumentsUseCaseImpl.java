package co.com.bancolombia.digitalizationprocesseddocuments;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;
import co.com.bancolombia.model.digitalizationprocesseddocuments.gateways.DigitalizationProcessedDocumentsRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DigitalizationProcessedDocumentsUseCaseImpl implements DigitalizationProcessedDocumentsUseCase {

    private final DigitalizationProcessedDocumentsRepository repository;

    @Override
    public DigitalizationProcessedDocuments save(DigitalizationProcessedDocuments digitalizationProcessedDocuments) {
        return repository.save(digitalizationProcessedDocuments);
    }

    @Override
    public Optional<DigitalizationProcessedDocuments> findByAcquisition(Acquisition acquisition) {
        return Optional.ofNullable(repository.findByAcquisition(acquisition));
    }

    @Override
    public Optional<DigitalizationProcessedDocuments> findLastDigitalizationProcessedDocuments(
            Acquisition acquisition, String documentalTypeCode, String documentalSubTypeCode) {
        return Optional.ofNullable(repository.findLastDigitalizationProcessedDocuments(
                acquisition, documentalTypeCode, documentalSubTypeCode));
    }
}
