package co.com.bancolombia.model.digitalizationprocesseddocuments.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.digitalizationprocesseddocuments.DigitalizationProcessedDocuments;

public interface DigitalizationProcessedDocumentsRepository {
    DigitalizationProcessedDocuments save (DigitalizationProcessedDocuments digitalizationProcessedDocuments);

    DigitalizationProcessedDocuments findByAcquisition (Acquisition acquisition);

    DigitalizationProcessedDocuments findLastDigitalizationProcessedDocuments (
            Acquisition acquisition, String documentalTypeCode, String documentalSubTypeCode);
}
