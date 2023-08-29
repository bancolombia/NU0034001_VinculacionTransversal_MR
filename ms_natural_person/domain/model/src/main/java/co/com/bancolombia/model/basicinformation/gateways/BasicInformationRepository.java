package co.com.bancolombia.model.basicinformation.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.basicinformation.BasicInformation;

public interface BasicInformationRepository {
    BasicInformation save(BasicInformation basicInformation);
    BasicInformation findByAcquisition(Acquisition acquisition);
}
