package co.com.bancolombia.validatesession;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.validatesession.ValidateSession;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;

public interface ValidateSessionUseCase {
    ValidateSessionResponse validateValidSession(String documentNumber, String documentType, String token,
                                                 String operation);

    ValidateSession saveValidateSession(ValidateSessionResponse validateSessionResponse, Acquisition acquisition);
    
    InfoReuseCommon getInfoReuseFromValidateSession(ValidateSessionResponse validateSessionResponse);
}
