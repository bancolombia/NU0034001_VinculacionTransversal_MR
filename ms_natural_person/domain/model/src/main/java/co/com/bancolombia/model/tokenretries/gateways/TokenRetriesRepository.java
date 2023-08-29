package co.com.bancolombia.model.tokenretries.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.tokenretries.TokenRetries;

public interface TokenRetriesRepository {

    TokenRetries findByAcquisition(Acquisition acquisition);
    TokenRetries save(TokenRetries tokenRetries);
}
