package co.com.bancolombia.documentretries;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.documentretries.DocumentRetries;

import java.util.Optional;

public interface DocumentRetriesUseCase {
    DocumentRetries save(Acquisition acquisition);

    Optional<DocumentRetries> findByAcquisition(Acquisition acquisition);
}
