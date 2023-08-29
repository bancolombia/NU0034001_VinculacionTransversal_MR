package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.markcustomer.RegisterControlListError;
import co.com.bancolombia.model.markcustomer.RegisterControlListErrorResponse;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_BP0008;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MC_BP0016;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_MARK_CUSTOMER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPE_MARKCUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VALIDATION_ERRS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers.TWO;

@RequiredArgsConstructor
public class MarkCustomerValidationsUseCaseImpl implements MarkCustomerValidationsUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    @Override
    public void validateResponse(RegisterControlListErrorResponse response, Acquisition acquisition) {
        RegisterControlListError registerError = response.getErrors().get(0);
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (registerError.getCode().charAt(0) == VALIDATION_ERRS) {
            vinculationUpdateUseCase.markOperation
                    (acquisition.getId().toString(), OPE_MARKCUSTOMER_VALUE, CODE_ST_OPE_RECHAZADO);
            vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), TWO.getNumber());
            error.put(ERROR_CODE_SYSTEM, Collections.singletonList(ErrorField.builder().name(OPER_MARK_CUSTOMER)
                    .complement(CODE_ERROR.concat(registerError.getCode()).concat(SPACE)
                            .concat(TITLE).concat(registerError.getTitle()).concat(SPACE)
                            .concat(DETAIL).concat(registerError.getDetail())).build()));
            throw new ValidationException(error);
        } else {
            if (MC_BP0016.equalsIgnoreCase(registerError.getCode())
                    || MC_BP0008.equalsIgnoreCase(registerError.getCode())) {
                vinculationUpdateUseCase.markOperation
                        (acquisition.getId().toString(), OPE_MARKCUSTOMER_VALUE, CODE_ST_OPE_RECHAZADO);
                vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), TWO.getNumber());
            }
            error.put(ERROR_CODE_VIDENTITY_BACKEND, Collections.singletonList(ErrorField.builder()
                    .name(OPER_MARK_CUSTOMER).complement(CODE_ERROR.concat(registerError.getCode())
                            .concat(SPACE).concat(TITLE).concat(registerError.getTitle())
                            .concat(SPACE).concat(DETAIL).concat(registerError.getDetail())).build()));
            throw new ValidationException(error);
        }
    }
}
