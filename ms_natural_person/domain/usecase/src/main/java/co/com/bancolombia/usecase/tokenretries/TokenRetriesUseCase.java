package co.com.bancolombia.usecase.tokenretries;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.tokenretries.TokenRetries;

import java.util.Optional;

public interface TokenRetriesUseCase {

    Optional<TokenRetries> findByAcquisition(Acquisition acquisition);
    TokenRetries save(TokenRetries tokenRetries);
    TokenRetries initTokenRetries (Acquisition acquisition, String userTransaction);
}
