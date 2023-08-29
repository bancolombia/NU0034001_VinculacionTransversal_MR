package co.com.bancolombia.execfield;

import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;

import java.util.List;

public interface ExecFieldUseCase {

    /**
     * This function creates the corresponding Exec-Field records in the DB given an M-Acquisition and a Checklist.
     * Exec-Field is created by the combination of Field and M-AcquisitionField. From Field, it takes the code and name.
     * From M-AcquisitionField, it takes de mandatory field.
     * The new records are related to the given checklist.
     *
     * @param matrixAcquisition
     * @param checklist
     */
    void insertExecFieldByMAcqAndChecklist(MatrixAcquisition matrixAcquisition, CheckList checklist);

    /**
     * Retrieves the list of Exec-Field related to the given CheckList
     *
     * @param checkList
     * @return
     */
    List<ExecField> findByChecklist(CheckList checkList);
}
