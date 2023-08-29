package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.markcustomer.RegisterControlListErrorResponse;

public interface MarkCustomerValidationsUseCase {

    void validateResponse(RegisterControlListErrorResponse response, Acquisition acquisition);
}
