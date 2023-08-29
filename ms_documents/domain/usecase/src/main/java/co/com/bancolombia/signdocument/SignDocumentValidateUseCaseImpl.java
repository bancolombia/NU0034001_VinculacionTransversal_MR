package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.signdocument.SDResponseError;
import co.com.bancolombia.model.signdocument.SDResponseErrorItem;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateTwoUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SIGNDOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_SIGN_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_SIGN_DOC_RETRIES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SIGN_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ERROR_ES_BVNT020_NO_RETRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ERROR_ES_BVNT020_RETRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ES_EXCEEDED_RETRIES;

@RequiredArgsConstructor
public class SignDocumentValidateUseCaseImpl implements SignDocumentValidateUseCase {

    private final ParametersUseCase parametersUseCase;
    private final Exceptions exceptions;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final VinculationUpdateTwoUseCase vinculationUpdateTwoUseCase;

    public void actionsErrors(Acquisition acquisition, SDResponseError responseError) {
        SDResponseErrorItem errorItem = responseError.getErrors().get(0);
        String code = errorItem.getCode();
        String codeException = ERROR_CODE_SYSTEM;
        if (Arrays.asList(ERROR_ES_BVNT020_RETRY).contains(code)) {
            actionErrorsRetry(acquisition, errorItem);
        } else if (Arrays.asList(ERROR_ES_BVNT020_NO_RETRY).contains(code)) {
            codeException = ERROR_CODE_VIDENTITY_BACKEND;
        }
        updateOperationsStep(acquisition);
        exceptions.createException(null, OPER_SIGN_DOCUMENT, createErrorComplement(errorItem), codeException);
    }

    public void actionErrorsRetry(Acquisition acquisition, SDResponseErrorItem errorItem) {
        List<Parameters> maxRetries = parametersUseCase.findByParent(PARAMETER_SIGN_DOC_RETRIES);
        int retries = acquisition.getSignDocRetries() + 1;
        if (retries <= Integer.parseInt(maxRetries.get(0).getCode())) {
            acquisition.setSignDocRetries(retries);
            vinculationUpdateTwoUseCase.updateAcquisition(acquisition.getId().toString());
            exceptions.createException(null, OPER_SIGN_DOCUMENT, createErrorComplement(errorItem),
                    ERROR_CODE_SIGN_DOCUMENT);
        } else {
            updateOperationsStep(acquisition);
            exceptions.createException(null, OPER_SIGN_DOCUMENT, ES_EXCEEDED_RETRIES, ERROR_CODE_VIDENTITY_BACKEND);
        }
    }

    public String createErrorComplement(SDResponseErrorItem errorItem) {
        return CODE_ERROR.concat(errorItem.getCode()).concat(SPACE).concat(DETAIL).concat(errorItem.getDetail());
    }

    public void updateOperationsStep(Acquisition acquisition) {
        vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_SIGNDOCUMENT, Numbers.THREE.getNumber());
        vinculationUpdateUseCase.updateAcquisition(acquisition.getId().toString(), Numbers.TWO.getNumber());
    }
}
