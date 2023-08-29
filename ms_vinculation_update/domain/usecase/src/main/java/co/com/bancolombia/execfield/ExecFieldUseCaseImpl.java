package co.com.bancolombia.execfield;

import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.model.execfield.gateways.ExecFieldRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ExecFieldUseCaseImpl implements ExecFieldUseCase {

    private final ExecFieldRepository execFieldRepository;

    @Override
    public void insertExecFieldByMAcqAndChecklist(MatrixAcquisition matrixAcquisition, CheckList checklist) {
        this.execFieldRepository.insertExecFieldByMAcqAndChecklist(matrixAcquisition, checklist);
    }

    @Override
    public List<ExecField> findByChecklist(CheckList checkList) {
        return this.execFieldRepository.findByChecklist(checkList);
    }
}
