package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.markcustomer.MarkCustomer;
import co.com.bancolombia.model.markcustomer.RegisterControlListRequest;

public interface MarkCustomerUseCase {

    MarkCustomer startProcessMarkOperation(BasicAcquisitionRequest basicAcquisitionRequest,
                                           Acquisition acquisition);

    RegisterControlListRequest createRequest(Acquisition acquisition);
}
