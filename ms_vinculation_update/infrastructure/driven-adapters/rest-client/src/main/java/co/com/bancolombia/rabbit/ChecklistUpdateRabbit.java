package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistUpdateQuery;
import co.com.bancolombia.model.checklist.gateways.CheckListUpdateRabbitRepository;
import co.com.bancolombia.model.statestep.gateways.StateStepRepository;
import co.com.bancolombia.model.step.gateways.StepRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOES_NOT_EXIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.util.constants.Constants.ACQUISITION;
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_STATE_STEP_NOT_EXISTS;
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_STEP_NOT_EXISTS;

@Component
@AllArgsConstructor
public class ChecklistUpdateRabbit extends ErrorHandleRabbit implements CheckListUpdateRabbitRepository {

    private final AcquisitionUseCase acquisitionUseCase;
    private final CheckListUseCase checkListUseCase;
    private final StepRepository stepRepository;
    private final StateStepRepository stateStepRepository;

    @Override
    public EmptyReply updateChecklistReply(ChecklistUpdateQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acq = acquisitionUseCase.findById(UUID.fromString(query.getAcquisitionId()));
            if (acq == null) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }

            Optional<Step> step = stepRepository.findByCode(query.getStepCode());
            if (!step.isPresent()) {
                String message = String.format(ERROR_MSG_STEP_NOT_EXISTS, query.getStepCode());
                throwExceptionRabbit(message);
            }

            Optional<StateStep> stateStep = stateStepRepository.findByCode(query.getStateCode());
            if (!stateStep.isPresent()) {
                String message = String.format(ERROR_MSG_STATE_STEP_NOT_EXISTS, query.getStateCode());
                throwExceptionRabbit(message);
            }

            checkListUseCase.markOperation(UUID.fromString(query.getAcquisitionId()),
                    query.getStepCode(), query.getStateCode());

            EmptyReply reply = EmptyReply.builder()
                    .valid(true)
                    .acquisitionId(query.getAcquisitionId())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            EmptyReply reply = EmptyReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(ChecklistUpdateQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
            if (query.getStateCode() == null || query.getStateCode().isEmpty()) {
                fieldList.add("stateCode");
            }
            if (query.getStepCode() == null || query.getStepCode().isEmpty()) {
                fieldList.add("stepCode");
            }
        }
        errorMandatory(fieldList);
    }
}