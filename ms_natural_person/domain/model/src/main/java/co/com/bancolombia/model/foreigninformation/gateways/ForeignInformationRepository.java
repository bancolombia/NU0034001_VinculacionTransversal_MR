package co.com.bancolombia.model.foreigninformation.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;

public interface ForeignInformationRepository {
    ForeignInformation findByAcquisition(Acquisition acquisition);
    ForeignInformation save(ForeignInformation instruction);
}