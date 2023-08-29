package co.com.bancolombia.usecase.validatelegalrep;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.validatelegalrep.ValidateLegalRep;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_LINK;

@RequiredArgsConstructor
public class ValidateLegalRepUseCaseImp implements ValidateLegalRepUseCase {

    @Override
    public ValidateLegalRep startProcessValidateLegalRep(Acquisition acquisition, String operation) {

        if (acquisition == null) {
            HashMap<String, List<ErrorField>> map = new HashMap<>();
            map.put(ERROR_CODE_WITHOUT_LINK, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(map);
        }

        ValidateLegalRep validateLegalRep = ValidateLegalRep.builder()
                .validationCode("0").validationDescription("Validaci\u00F3n exitosa").build();

        return validateLegalRep;
    }
}
