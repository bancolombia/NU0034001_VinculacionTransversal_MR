package co.com.bancolombia.restclient.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.UpdateCiiuQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.query.ControlListSaveQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.ControlListSaveReply;
import co.com.bancolombia.controllist.ControlListUseCase;
import co.com.bancolombia.model.controllist.gateways.ControlListSaveRabbitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_STATE_CONTROL_LIST_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ControlListSaveRabbit extends ErrorHandleRabbit implements ControlListSaveRabbitRepository {

    private final ControlListUseCase controlListUseCase;

    @Override
    public ControlListSaveReply findStateValidationCustomerControlList(ControlListSaveQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            String state =
                    controlListUseCase.findStateValidationCustomerControlList(
                            query.getAcquisitionId(),
                            query.getDocumentType(),
                            query.getDocumentNumber()
                    );
            if (state == null) {
                String message = ERROR_MSG_STATE_CONTROL_LIST_NOT_FOUND + query.getAcquisitionId();
                throwExceptionRabbit(message);
            }

            return ControlListSaveReply.builder()
                    .controlListValidationState(state)
                    .valid(true)
                    .build();
        } catch (ValidationException | CustomException ex) {
            ControlListSaveReply reply =
                    ControlListSaveReply.builder()
                            .valid(false)
                            .errorList(getErrorFromException(ex))
                            .build();
            return reply;
        }
    }

    private void validateMandatory(ControlListSaveQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
            if (query.getDocumentType() == null || query.getDocumentType().isEmpty()) {
                fieldList.add("documentType");
            }
            if (query.getDocumentNumber() == null || query.getDocumentNumber().isEmpty()) {
                fieldList.add("documentNumber");
            }
        }
        errorMandatory(fieldList);
    }
}
