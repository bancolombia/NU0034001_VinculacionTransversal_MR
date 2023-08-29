package co.com.bancolombia.usecase.economicinformation;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.economicinformation.EconomicInformation;

import java.util.Optional;

public interface EconomicInformationUseCase {
    /**
     * This function creates the economic information.
     * The acquisition is validated with the given id, but before calling this function, the docType parameters,
     * docNumber and acqId must be validated with AcquisitionUseCase.validateInfoSearchAndGet.
     * @param economicInformation
     */
    EconomicInformation startProcessEconomicInformation(EconomicInformation economicInformation);

    /**
     * this function allows to mark the detail of the economic activity
     * @param economicActivity
     * @return
     */
    String economicActivityMark(String economicActivity);

    EconomicInformation save(EconomicInformation economicInformation);

    Optional<EconomicInformation> findByAcquisition(Acquisition acquisition);
}
