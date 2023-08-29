package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.model.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponseWithLog;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRepository;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRestRepository;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCase;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_VALIDATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATUS_OK;

@RequiredArgsConstructor
public class ValidateTokenUseCaseImpl implements ValidateTokenUseCase {

    private final ValidateTokenRepository validateTokenRepository;
    private final ValidateTokenMapperUseCase validateTokenMapperUseCase;
    private final ValidateTokenRestRepository validateTokenRestRepository;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final TokenRetriesUseCase tokenRetriesUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final ValidateTokenValidationUseCase validateTokenValidationUseCase;
    private final ValidateTokenCreateRequestUseCase createRequest;
    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_VALIDATION, OPER_TOKEN_OPERATION);

    @Override
    public ValidateToken saveInfo(ValidateToken validateToken) {
        return validateTokenRepository.save(validateToken);
    }

    @Override
    public ValidateToken findByAcquisition(Acquisition acquisition){
        return validateTokenRepository.findByAcquisition(acquisition);
    }

    @Override
    public ValidateToken findByAcquisitionLast(Acquisition acquisition){
        return validateTokenRepository.findByAcquisitionLast(acquisition);
    }

    @Override
    public ValidateToken startProcessValidateToken(BasicAcquisitionRequest request, ValidateToken validateToken) {
        Acquisition acquisition = validateToken.getAcquisition();
        String userTransaction = request.getUserTransaction();

        TokenRetries tokenRetries = tokenRetriesUseCase.initTokenRetries(acquisition, userTransaction);

        String cellphone = generateTokenUseCase.getCellphoneByLastToken(acquisition);
        String blockMsj = validateTokenValidationUseCase.validateRetries(acquisition, tokenRetries, userTransaction);
        validateTokenValidationUseCase.validateErrors(cellphone, blockMsj);

        ValidateTokenRequest validateTokenRequest = createRequest.createRequest(
                acquisition, request.getDocumentNumber(), validateToken.getTokenCode(), cellphone);
        adapter.info(CONSUME_EXTERNAL);
        ValidateTokenResponseWithLog validateTokenResponseWithLog =
                validateTokenRestRepository.getUserInfoFromValidateToken(
                        validateTokenRequest, request.getMessageId(), coreFunctionDate.getDatetime());
        adapter.info(CONSUME_EXTERNAL_RESULT+STATUS_OK);
        ValidateTokenResponse validateTokenResponse = validateTokenResponseWithLog.getValidateTokenResponse();

        validateTokenValidationUseCase.validateExceptionError(validateTokenResponse);

        ValidateToken validateTokenSaved = saveInfo(
                validateTokenMapperUseCase.mapperFromValidateToken
                        (validateToken, validateTokenResponse, request, cellphone));
        validateTokenSaved.setInfoReuseCommon(validateTokenResponseWithLog.getInfoReuseCommon());
        vinculationUpdateUseCase.markOperation
                (acquisition.getId(),CODE_VALIDATE_TOKEN, CODE_ST_OPE_COMPLETADO);
        return validateTokenSaved;
    }
}