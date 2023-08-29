package co.com.bancolombia.checklist;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;

import java.util.List;
import java.util.UUID;

public interface CheckListValidationUseCase {

    /**
     * Returns the fields according to the acq and stepCode.
     * The acquisition with the given id is validated, but before calling this function, the parameters docType,
     * docNumber and acqId must be validated with AcquisitionUseCase.validateInfoSearchAndGet.
     *
     * @param acqId
     * @param stepCode
     * @return List of codes of mandatory fields.
     */
    List<ExecField> getExecFieldListByStep(UUID acqId, String stepCode);

    CheckList getCheckListByAcquisitionAndStep(Acquisition acquisition, String stepCode);
}
