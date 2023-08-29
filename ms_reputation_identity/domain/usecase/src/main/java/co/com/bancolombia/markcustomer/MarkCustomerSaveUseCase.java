package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.markcustomer.RegisterControlListResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListSave;

public interface MarkCustomerSaveUseCase {

    void saveInfo(RegisterControlListResponse response, Acquisition acquisition,
                         BasicAcquisitionRequest request);

    RegisterControlListSave transSave(RegisterControlListResponse response, Acquisition acquisition,
                                             BasicAcquisitionRequest ba);
}
