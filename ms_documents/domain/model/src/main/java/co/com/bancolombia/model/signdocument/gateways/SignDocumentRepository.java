package co.com.bancolombia.model.signdocument.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.signdocument.SignDocument;

public interface SignDocumentRepository {

    SignDocument findByAcquisition(Acquisition acquisition);
    SignDocument save(SignDocument signDocument);
}
