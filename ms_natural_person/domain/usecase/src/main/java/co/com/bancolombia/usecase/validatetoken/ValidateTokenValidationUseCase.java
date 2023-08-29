package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_BLOQUEADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_VAL_TOKEN_MAX_RETRIES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_VAL_TOKEN_TIME_BLOCK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZERO;

@RequiredArgsConstructor
public class ValidateTokenValidationUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final TokenRetriesUseCase tokenRetriesUseCase;
    private final ParametersUseCase parametersUseCase;
    private final Exceptions exceptions;


    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_VALIDATION, OPER_TOKEN_OPERATION);

    public String validateRetries(Acquisition acquisition, TokenRetries tokenRetries, String userTransaction){
        String blockMsj = null;
        List<Parameters> parameterRetries = parametersUseCase.findByParent(PARAMETER_VAL_TOKEN_MAX_RETRIES);
        int retries = tokenRetries.getValidateTokenRetries()+1;
        if(retries > Integer.parseInt(parameterRetries.get(0).getCode())) {
            ChecklistReply checklist = vinculationUpdateUseCase.checkListStatus(acquisition.getId(),
                    Constants.CODE_VALIDATE_TOKEN);
            String parameterBlockTime = parametersUseCase.findByParent(PARAMETER_VAL_TOKEN_TIME_BLOCK)
                    .get(0).getCode();

            if(CODE_ST_OPE_BLOQUEADO.equals(checklist.getStateOperation())){
                blockMsj = validateTokenUnBlock(acquisition, parameterBlockTime, tokenRetries);
            }else {
                blockMsj = coreFunctionDate.minutesFormat(Integer.parseInt(parameterBlockTime));
                tokenRetries.setValidateTokenLockDate(coreFunctionDate.getDatetime());
                vinculationUpdateUseCase.markOperation
                        (acquisition.getId(), Constants.CODE_VALIDATE_TOKEN, Constants.CODE_ST_OPE_BLOQUEADO);
            }
        }else{
            tokenRetries.setValidateTokenRetries(retries);
        }
        tokenRetries.setUpdatedBy(userTransaction);
        tokenRetries.setUpdatedDate(coreFunctionDate.getDatetime());
        tokenRetriesUseCase.save(tokenRetries);
        return blockMsj;
    }

    public String validateTokenUnBlock(Acquisition acquisition, String parameterBlockTime, TokenRetries tokenRetries){
        String difference = coreFunctionDate.compareDifferenceTime
                (tokenRetries.getValidateTokenLockDate(), parameterBlockTime, true, true);
        if (difference!=null){
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ConstantsErrors.ERROR_CODE_MAX_RETRIES, Collections.singletonList(ErrorField.builder()
                    .name(difference).build()));
            adapter.error(ConstantsErrors.ERROR_CODE_MAX_RETRIES+MIDDLE_SCREEN+difference);
            throw new ValidationException(error);
        }else{
            vinculationUpdateUseCase.markOperation
                    (acquisition.getId(), Constants.CODE_VALIDATE_TOKEN, Constants.CODE_ST_OPE_PENDIENTE);
            tokenRetries.setValidateTokenRetries(ZERO);
        }
        return difference;
    }



    public void validateErrors(String cellphone, String blockMsj){
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> errorFields = new ArrayList<>();

        if (blockMsj!=null){
            errorFields.add(ErrorField.builder().name(blockMsj).build());
            error.put(ConstantsErrors.ERROR_CODE_MAX_RETRIES, errorFields);
            adapter.error(ConstantsErrors.ERROR_CODE_MAX_RETRIES+MIDDLE_SCREEN+errorFields);
        }else if(cellphone==null || cellphone.isEmpty()){
            errorFields.add(ErrorField.builder().name(Constants.CELLPHONE).build());
            error.put(ConstantsErrors.ERROR_CODE_EMAIL_CELLPHONE, errorFields);
            adapter.error(ConstantsErrors.ERROR_CODE_EMAIL_CELLPHONE+MIDDLE_SCREEN+errorFields);
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    public void validateExceptionError(ValidateTokenResponse validateTokenResponse) {
        if(validateTokenResponse.getErrors()!=null){
            exceptions.createException
                    (null, validateTokenResponse.getErrors().get(0).getDetail(), EMPTY,
                            ConstantsErrors.ERROR_CODE_INVALID_TOKEN);
        }
    }
}
