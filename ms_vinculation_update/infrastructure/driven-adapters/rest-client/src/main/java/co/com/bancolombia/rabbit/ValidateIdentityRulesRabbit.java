package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.validateidentityrules.ValidateIdentityRules;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.model.validateidentityrules.gateways.ValidateIdentityRulesRabbitRepository;
import co.com.bancolombia.model.validateidentityrules.gateways.ValidateIdentityRulesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOES_NOT_EXIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.util.constants.Constants.ACQUISITION;

@Component
@AllArgsConstructor
public class ValidateIdentityRulesRabbit extends ErrorHandleRabbit implements ValidateIdentityRulesRabbitRepository {

    private final ValidateIdentityRulesRepository validateIdentityRulesRepository;
    private final AcquisitionUseCase acquisitionUseCase;

    @Override
    public ValidateIdentityRulesReply validateIdentityRules(AcquisitionIdQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acquisition = acquisitionUseCase.findById(UUID.fromString(query.getAcquisitionId()));
            if (acquisition == null) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }

            List<ValidateIdentityRules> validateIdentityRulesList = validateIdentityRulesRepository
                    .findByTypeAcquisition(acquisition.getTypeAcquisition());
            List<ValidateIdentityRuleReply> validateIdentityRulesReplies = new ArrayList<>();
            validateIdentityRulesList.forEach(item -> {
                ValidateIdentityRuleReply reply = ValidateIdentityRuleReply.builder()
                        .active(Boolean.toString(item.isActive()))
                        .name(item.getRule()).score(item.getScore().toString()).build();
                validateIdentityRulesReplies.add(reply);
            });

            ValidateIdentityRulesReply reply = ValidateIdentityRulesReply.builder()
                    .valid(true)
                    .typeAcquisition(acquisition.getTypeAcquisition().getCode())
                    .validateIdentityRulesList(validateIdentityRulesReplies).build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            ValidateIdentityRulesReply reply = ValidateIdentityRulesReply.builder()
                    .valid(false)
                    .errorList(getErrorFromException(ex)).build();

            return reply;
        }
    }

    private void validateMandatory(AcquisitionIdQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
        }
        errorMandatory(fieldList);
    }
}
