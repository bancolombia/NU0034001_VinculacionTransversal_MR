package co.com.bancolombia.prerequisitesstep;

import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.prerequisitesstep.PrerequisitesStep;
import co.com.bancolombia.model.prerequisitesstep.gateways.PrerequisitesStepRepository;
import co.com.bancolombia.step.StepUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PENDING_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_MSG_STATE_NOT_FOUND_DB;

@RequiredArgsConstructor
public class PrerequisitesStepUseCaseImpl implements PrerequisitesStepUseCase {

    private final PrerequisitesStepRepository prerequisitesStepRepository;
    private final StepUseCase stepUseCase;
    private final CheckListUseCase checkListUseCase;

    @Override
    public List<PrerequisitesStep> findByTypeAcquisitionAndCurrentStep(TypeAcquisition typeAcquisition,
                                                                       String currentStepCode) {
        Step currentStep = this.stepUseCase.findByCode(currentStepCode)
                .orElseThrow(() -> new CustomException(ERROR_MSG_STATE_NOT_FOUND_DB));
        return prerequisitesStepRepository.findByTypeAcquisitionAndCurrentStep(typeAcquisition, currentStep);
    }

    @Override
    public void validatePrerrequisites(Acquisition acquisition, String currentStepCode) {
        List<CheckList> checkList = checkListUseCase.getCheckListByAcquisition(acquisition);
        List<PrerequisitesStep> prerequisites = this.findByTypeAcquisitionAndCurrentStep
                (acquisition.getTypeAcquisition(), currentStepCode);
        List<CheckList> filteredList = checkList.stream()
                .filter(check -> prerequisites.stream()
                        .anyMatch(pre -> check.getStep().getCode().equals(pre.getStep().getCode())))
                .collect(Collectors.toList());
        List<ErrorField> errorFields = new ArrayList<>();
        filteredList.stream().forEach(check -> {
            prerequisites.stream().filter
                    (pre -> check.getStep().getCode().equals(pre.getStep().getCode())).findFirst()
                    .get().getStates();
            if (!prerequisites.stream().filter
                    (pre -> check.getStep().getCode().equals(pre.getStep().getCode())).findFirst().get().
                    getStates().contains(check.getState().getCode())) {
                errorFields.add(ErrorField.builder().name(check.getStep().getOperation()).build());
            }
        });

        if (!errorFields.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_PENDING_OPERATION, errorFields);
            throw new ValidationException(error);
        }
    }
}
