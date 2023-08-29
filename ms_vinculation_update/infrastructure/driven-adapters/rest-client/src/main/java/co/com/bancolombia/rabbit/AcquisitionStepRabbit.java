package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionStepRabbitRepository;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
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
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_CHECKLIST_NOT_EXISTS;
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_STEP_NOT_EXISTS;

@Component
@AllArgsConstructor
public class AcquisitionStepRabbit extends ErrorHandleRabbit implements AcquisitionStepRabbitRepository {

    private final AcquisitionUseCase acquisitionUseCase;
    private final StepRepository stepRepository;
    private final CheckListRepository checkListRepository;

    @Override
    public AcquisitionStateReply getAcquisitionStepState(AcquisitionStateQuery query) {
        try {
            validateMandatory(query);
            if (query.getAcquisitionId() != null) {
                validateUUID(query.getAcquisitionId());
            }

            Acquisition acquisition = query.getAcquisitionId() == null
                    ? acquisitionUseCase.findByDocumentTypeAndDocumentNumber(
                            query.getDocumentType(), query.getDocumentNumber())
                    : acquisitionUseCase.findByIdWitOutState(UUID.fromString(query.getAcquisitionId()));
            if (acquisition == null) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }

            Optional<Step> step = stepRepository.findByCode(query.getOperation());
            if (!step.isPresent()) {
                String message = String.format(ERROR_MSG_STEP_NOT_EXISTS, query.getOperation());
                throwExceptionRabbit(message);
            }

            CheckList checkList = checkListRepository.findByAcquisitionAndStep(acquisition, step.get());
            if (checkList == null) {
                throwExceptionRabbit(ERROR_MSG_CHECKLIST_NOT_EXISTS);
            }

            AcquisitionStateReply reply = AcquisitionStateReply.builder()
                    .valid(true)
                    .documentNumber(acquisition.getDocumentNumber())
                    .documentType(acquisition.getDocumentType().getCode())
                    .operation(query.getOperation())
                    .acquisitionId(acquisition.getId().toString())
                    .stateAcquisitionCode(acquisition.getStateAcquisition().getCode())
                    .stateAcquisitionName(acquisition.getStateAcquisition().getName())
                    .stateStepCode(checkList.getState().getCode())
                    .stateStepName(checkList.getState().getName())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            AcquisitionStateReply reply = AcquisitionStateReply.builder()
                    .valid(false)
                    .documentNumber(query == null ? "" : query.getDocumentNumber())
                    .documentType(query == null ? "" : query.getDocumentType())
                    .operation(query == null ? "" : query.getOperation())
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(AcquisitionStateQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getOperation() == null || query.getOperation().isEmpty()) {
                fieldList.add("operation");
            }
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                if ((query.getDocumentType() == null || query.getDocumentType().isEmpty())
                        && (query.getDocumentNumber() == null || query.getDocumentNumber().isEmpty())) {
                    fieldList.add("acquisitionId");
                } else {
                    if (query.getDocumentType() == null || query.getDocumentType().isEmpty()) {
                        fieldList.add("documentType");
                    }
                    if (query.getDocumentNumber() == null || query.getDocumentNumber().isEmpty()) {
                        fieldList.add("documentNumber");
                    }
                }
            }
        }
        errorMandatory(fieldList);
    }
}
