package co.com.bancolombia.model.validatesession.gateways;

import co.com.bancolombia.model.validatesession.ValidateSessionRequest;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;

public interface ValidateSessionRestRepository {
    ValidateSessionResponse getTokenValidation(ValidateSessionRequest validateSessionRequest, String operation);
}
