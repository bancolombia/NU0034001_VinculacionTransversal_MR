package co.com.bancolombia.usecase.validatelegalrep;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.validatelegalrep.ValidateLegalRep;

public interface ValidateLegalRepUseCase {
    ValidateLegalRep startProcessValidateLegalRep(Acquisition acquisition, String operation);
}
