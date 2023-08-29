package co.com.bancolombia.checklist;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.execfield.ExecFieldUseCase;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.step.StepUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_MSG_STATE_NOT_FOUND_DB;

@RequiredArgsConstructor
public class CheckListValidationUseCaseImpl implements CheckListValidationUseCase {

    private final StepUseCase stepUseCase;
    private final AcquisitionUseCase acquisitionUseCase;
    private final CheckListRepository checkListRepository;
    private final ExecFieldUseCase execFieldUseCase;

    @Override
    public List<ExecField> getExecFieldListByStep(UUID acqId, String stepCode) {
        Acquisition acquisition = this.acquisitionUseCase.findAndValidateById(acqId);
        Step step = this.stepUseCase.findByCode(stepCode)
                .orElseThrow(() -> new CustomException(ERROR_MSG_STATE_NOT_FOUND_DB));
        CheckList checkList = this.checkListRepository.findByAcquisitionAndStep(acquisition, step);
        return this.execFieldUseCase.findByChecklist(checkList);
    }

    @Override
    public CheckList getCheckListByAcquisitionAndStep(Acquisition acquisition, String stepCode) {
        Step step = this.stepUseCase.findByCode(stepCode)
                .orElseThrow(() -> new CustomException(ERROR_MSG_STATE_NOT_FOUND_DB));
        return this.checkListRepository.findByAcquisitionAndStep(acquisition, step);
    }
}
