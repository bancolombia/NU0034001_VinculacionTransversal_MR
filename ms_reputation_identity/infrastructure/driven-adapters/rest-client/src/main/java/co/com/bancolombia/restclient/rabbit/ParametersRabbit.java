package co.com.bancolombia.restclient.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.query.ParameterQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.ParameterReply;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRabbitRepository;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.util.constants.Constants.ERROR_MSG_PARAMETER_NOT_FOUND;

@Component
@AllArgsConstructor
public class ParametersRabbit extends ErrorHandleRabbit implements ParametersRabbitRepository {

    private ParametersRepository parametersRepository;

    public ParameterReply findByNameParent(ParameterQuery query) {
        try {
            validateMandatory(query);
            Optional<Parameter> parameter = parametersRepository.findByNameParent(query.getName(), query.getParent());
            if (!parameter.isPresent()) {
                String message = ERROR_MSG_PARAMETER_NOT_FOUND + SPACE + query.getName() + SPACE + query.getParent();
                throwExceptionRabbit(message);
            }
            return ParameterReply.builder().code(parameter.get().getCode()).valid(true).build();
        } catch (ValidationException | CustomException ex) {
            return ParameterReply.builder().valid(false).errorList(getErrorFromException(ex)).build();
        }
    }

    private void validateMandatory(ParameterQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getName() == null || query.getName().isEmpty()) {
                fieldList.add("name");
            }
            if (query.getParent() == null || query.getParent().isEmpty()) {
                fieldList.add("parent");
            }
        }
        errorMandatory(fieldList);
    }
}
