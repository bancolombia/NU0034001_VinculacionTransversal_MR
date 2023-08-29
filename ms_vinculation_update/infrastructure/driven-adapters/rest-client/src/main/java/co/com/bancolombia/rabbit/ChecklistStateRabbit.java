package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.execfield.ExecField;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.model.checklist.gateways.CheckListStateRabbitRepository;
import co.com.bancolombia.model.execfield.gateways.ExecFieldRepository;
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
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_STEP_NOT_EXISTS;

@Component
@AllArgsConstructor
public class ChecklistStateRabbit extends ErrorHandleRabbit implements CheckListStateRabbitRepository {

    private final AcquisitionUseCase acquisitionUseCase;
    private final CheckListRepository checkListRepository;
    private final StepRepository stepRepository;
    private final ExecFieldRepository execFieldRepository;

    @Override
    public ChecklistReply stateChecklist(ChecklistQuery query){
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acquisition = acquisitionUseCase.findById(UUID.fromString(query.getAcquisitionId()));
            Optional<Step> step = stepRepository.findByCode(query.getOperation());
            if (acquisition == null) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }
            if (!step.isPresent()) {
                String message = String.format(ERROR_MSG_STEP_NOT_EXISTS, query.getOperation());
                throwExceptionRabbit(message);
            }

            CheckList checkList = checkListRepository.findByAcquisitionAndStep(acquisition, step.get());

            List<ExecField> execFieldList = execFieldRepository.findByChecklist(checkList);
            List<ExecFieldReply> execFieldReplies = new ArrayList<>();
            execFieldList.forEach(item -> {
                ExecFieldReply reply = ExecFieldReply.builder()
                        .code(item.getCode()).name(item.getName())
                        .mandatory(item.isMandatory()).upgradeable(item.isUpgradeable()).build();
                execFieldReplies.add(reply);
            });

            ChecklistReply reply = ChecklistReply.builder()
                    .valid(true)
                    .acquisitionId(acquisition.getId().toString())
                    .documentNumber(acquisition.getDocumentNumber())
                    .documentType(acquisition.getDocumentType().getCodeHomologation())
                    .stateOperation(checkList.getState().getCode())
                    .execFieldList(execFieldReplies)
                    .build();
            return reply;
        } catch (ValidationException | CustomException ex) {
            ChecklistReply reply = ChecklistReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(ChecklistQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
            if (query.getOperation() == null || query.getOperation().isEmpty()) {
                fieldList.add("operation");
            }
        }
        errorMandatory(fieldList);
    }
}