package co.com.bancolombia.model.economicinformation.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.economicinformation.EconomicInformation;

public interface EconomicInformationRepository {
    EconomicInformation save(EconomicInformation economicInformation);
    EconomicInformation findByAcquisition(Acquisition acquisition);
}

