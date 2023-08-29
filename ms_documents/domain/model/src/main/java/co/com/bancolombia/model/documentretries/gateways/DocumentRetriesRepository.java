package co.com.bancolombia.model.documentretries.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.documentretries.DocumentRetries;

public interface DocumentRetriesRepository {
    DocumentRetries save(DocumentRetries documentRetries);
    DocumentRetries findByAcquisition(Acquisition acquisition);
}
