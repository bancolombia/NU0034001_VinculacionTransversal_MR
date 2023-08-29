package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.BlockCustomerQuery;
import co.com.bancolombia.customercontrol.CustomerControlUseCase;
import co.com.bancolombia.model.customercontrol.gateways.CustomerControlRabbitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CustomerControlRabbit extends ErrorHandleRabbit implements CustomerControlRabbitRepository {

    private final CustomerControlUseCase customerControlUseCase;

    @Override
    public EmptyReply blockCustomer(BlockCustomerQuery query) {
        try {
            validateMandatory(query);

            customerControlUseCase.blockCustomer(
                    query.getDocumentType(), query.getDocumentNumber(), query.getUnlockDate(), query.getOperation());

            return EmptyReply.builder().valid(true).build();
        } catch (ValidationException | CustomException ex) {
            EmptyReply reply = EmptyReply.builder()
                    .valid(false)
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(BlockCustomerQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getDocumentNumber() == null || query.getDocumentNumber().isEmpty()) {
                fieldList.add("documentNumber");
            }
            if (query.getDocumentType() == null || query.getDocumentType().isEmpty()) {
                fieldList.add("documentType");
            }
            if (query.getUnlockDate() == null) {
                fieldList.add("unlockDate");
            }
            if (query.getOperation() == null || query.getOperation().isEmpty()) {
                fieldList.add("operation");
            }
        }
        errorMandatory(fieldList);
    }
}
