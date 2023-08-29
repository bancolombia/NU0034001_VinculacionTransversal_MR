package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_RABBIT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_RUN_TIME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_UUID;
import static co.com.bancolombia.usecase.util.constants.Constants.ERROR_MSG_INCOMPLETE_FIELDS;
import static co.com.bancolombia.usecase.util.constants.Constants.ERROR_MSG_WRONG_UUID;

public class ErrorHandleRabbit {

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_INFORMATION, "RABBIT ERROR");

    protected void throwExceptionRabbit(String message) {
        ErrorField field = getErrorItem(message, null, null);
        HashMap<String, List<ErrorField>> errors = new HashMap<>();
        adapter.error(ERROR + SPACE + ERROR_CODE_RABBIT + SPACE + message);
        errors.put(ERROR_CODE_RABBIT, Collections.singletonList(field));
        throw new ValidationException(errors);
    }

    protected Map<String, List<ErrorField>> getErrorFromException(Exception ex) {
        Map<String, List<ErrorField>> errors = ex.getClass().equals(ValidationException.class)
                ? ((ValidationException) ex).getErrorFieldsCodes()
                : getMapErrorRuntime(ex.getMessage());

        return errors;
    }

    protected Map<String, List<ErrorField>> getMapErrorRuntime(String message) {
        ErrorField field = getErrorItem(message, null, null);
        HashMap<String, List<ErrorField>> errors = new HashMap<>();
        adapter.error(ERROR + SPACE + ERROR_CODE_RUN_TIME + SPACE + message);
        errors.put(ERROR_CODE_RUN_TIME, Collections.singletonList(field));
        return errors;
    }

    protected void errorMandatory(List<String> fieldList) {
        if (!fieldList.isEmpty()) {
            String fields = fieldList.stream().collect(Collectors.joining(", "));
            String message = ERROR_MSG_INCOMPLETE_FIELDS + fields;
            throwExceptionRabbit(message);
        }
    }

    protected void validateUUID(String uuid) {
        if (!uuid.matches(REGEX_UUID)) {
            String message = String.format(ERROR_MSG_WRONG_UUID, uuid);
            throwExceptionRabbit(message);
        }
    }

    protected ErrorField getErrorItem(String name, String complement, String nameList) {
        return ErrorField.builder().name(name).complement(complement).nameList(nameList).build();
    }
}
