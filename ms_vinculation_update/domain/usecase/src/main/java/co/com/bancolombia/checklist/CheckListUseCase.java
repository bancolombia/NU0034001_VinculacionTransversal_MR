package co.com.bancolombia.checklist;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.startlist.StartList;

import java.util.List;
import java.util.UUID;

public interface CheckListUseCase {

    /**
     * This function creates the checklist associated with the acquisition.
     * The acquisition with the given id is validated, but before calling this function, the parameters docType,
     * docNumber and acqId must be validated with AcquisitionUseCase.validateInfoSearchAndGet.
     *
     * @param acqId
     * @param usrMod
     */
    public List<CheckList> createCheckList(UUID acqId, String usrMod);

    /**
     * Marks the operation with stepCode as stateCode. This function invoked by mark operation with state
     *
     * @param acqId
     * @param stepCode
     * @param stateCode
     */
    public void markOperation(UUID acqId, String stepCode, String stateCode);

    /**
     * Returns the activities related to the acq identified by acqId.
     *
     * @param acqId
     * @return List of activities.
     */
    public List<StartList> getProcessesCheckList(UUID acqId, String docType, String docNumber, String operation);

    /**
     * Returns the activities related to the acq.
     *
     * @param acquisition
     * @return List of activities.
     */
    public List<CheckList> getCheckListByAcquisition(Acquisition acquisition);
}
