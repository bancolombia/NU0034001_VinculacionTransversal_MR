package co.com.bancolombia.digitalizationprocesseddocuments;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;

import java.util.Optional;

public interface  DigitalizationProcessedDocumentsUseCase {
    DigitalizationProcessedDocuments save (DigitalizationProcessedDocuments digitalizationProcessedDocuments);

    Optional<DigitalizationProcessedDocuments> findByAcquisition (Acquisition acquisition);

    Optional<DigitalizationProcessedDocuments> findLastDigitalizationProcessedDocuments (
            Acquisition acquisition, String documentalTypeCode, String documentalSubTypeCode);
}
