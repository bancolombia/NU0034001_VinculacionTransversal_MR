package co.com.bancolombia.model.execfield.gateways;

import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;

import java.util.List;

public interface ExecFieldRepository {
    List<ExecField> insertExecFieldByMAcqAndChecklist(MatrixAcquisition matrixAcquisition, CheckList checkList);
    List<ExecField> findByChecklist(CheckList checkList);
}