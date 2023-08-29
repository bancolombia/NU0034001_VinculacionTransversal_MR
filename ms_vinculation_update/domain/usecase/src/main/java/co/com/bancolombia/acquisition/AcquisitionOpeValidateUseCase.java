package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;

public interface AcquisitionOpeValidateUseCase {
    public void validateCustomerControl(String docType, String docNumber);

    public ValidateSessionResponse getValidateSessionResponse(
            AcquisitionRequestModel acquisitionRequestModel, String operation);

    public void saveValidateSession(
            ValidateSessionResponse validateSessionResponse, Acquisition acquisition, String operation);
}
