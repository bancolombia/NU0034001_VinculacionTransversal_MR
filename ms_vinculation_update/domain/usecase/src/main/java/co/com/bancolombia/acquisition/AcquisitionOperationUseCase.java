package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;

import java.util.List;
import java.util.UUID;

public interface AcquisitionOperationUseCase {
    /**
     * this function performs the star-acquisition or start-update process
     *
     * @param acquisitionRequestModel
     * @param usrMod
     * @param operation
     * @return Acquisition
     */
    public Acquisition startAcquisition(AcquisitionRequestModel acquisitionRequestModel, String usrMod,
                                        String operation);

    /**
     * this function performs the storage of the requested acquisition
     *
     * @param acquisitionStartObjectModel
     * @param usrMod
     * @return Acquisition
     */
    public Acquisition saveAcquisition(AcquisitionStartObjectModel acquisitionStartObjectModel, String usrMod);

    /**
     * this function performs the storage of the requested acquisition
     *
     * @param acquisition
     * @return Acquisition
     */
    public Acquisition save(Acquisition acquisition);

    /**
     * this function performs the storage of the requested acquisition
     *
     * @param ocp
     * @param code
     * @return Acquisition
     */
    public void updateAcquisition(Acquisition ocp, String code);

    /**
     * this function count acquisitions by state storage
     *
     * @param state
     * @param acquisitionIdList
     * @return long
     */
    long countAcquisitionByState(String state, List<UUID> acquisitionIdList);
}
