package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRepository;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRestRepository;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCase;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_G_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;

@RequiredArgsConstructor
public class GenerateTokenUseCaseImpl implements GenerateTokenUseCase{

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final GenerateTokenRepository generateTokenRepository;
    private final PersonalInformationRepository personalInformationRepository;
    private final GenerateTokenValidationUseCase generateTokenValidationUseCase;
    private final GenerateTokenRestRepository generateTokenRestRepository;
    private final CoreFunctionDate coreFunctionDate;
    private final TokenRetriesUseCase tokenRetriesUseCase;
    private final GenerateTokenConstructUseCase generateTokenConstructUseCase;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_IDENTITY, OPER_G_TOKEN_OPERATION);

    public GenerateToken save(GenerateToken generateToken, InfoReuseCommon infoReuseCommon){
        generateTokenRepository.save(generateToken).setInfoReuseCommon(infoReuseCommon);
        return generateToken;
    }

    public GenerateToken startProcessGenerateToken(BasicAcquisitionRequest request, GenerateToken generateToken)
            throws ParseException {
        Acquisition acquisition = generateToken.getAcquisition();
        String userTransaction = request.getUserTransaction();

        TokenRetries tokenRetries = tokenRetriesUseCase.initTokenRetries(acquisition, userTransaction);

        String blockMsj = generateTokenValidationUseCase.validateRetries(acquisition, tokenRetries, userTransaction);
        generateToken = cellAndEmail(generateToken);
        generateTokenValidationUseCase.validateGenerateToken(generateToken, blockMsj);

        GTRequest generateTokenRequest = generateTokenConstructUseCase.createRequestGetToken(
                generateToken, request.getDocumentNumber(), acquisition.getDocumentType().getCode());

        adapter.info(Constants.CONSUME_EXTERNAL);
        GTResponseWithLog generateTokenResponseWithLog = generateTokenRestRepository.getToken(
                generateTokenRequest, request.getMessageId(), coreFunctionDate.getDatetime());
        adapter.info(Constants.CONSUME_EXTERNAL_RESULT.concat(generateTokenRestRepository.toString()));

        generateToken = generateTokenConstructUseCase.reFormatGenerateToken
                (generateToken, request, generateTokenResponseWithLog);

        vinculationUpdateUseCase.markOperation
                (acquisition.getId(), Constants.CODE_GENERATE_TOKEN, Constants.CODE_ST_OPE_COMPLETADO);
        return save(generateToken, generateTokenResponseWithLog.getInfoReuseCommon());
    }

    public GenerateToken cellAndEmail(GenerateToken generateToken) {
        PersonalInformation personalInformation = personalInformationRepository
                .findByAcquisition(generateToken.getAcquisition());
        if (personalInformation!=null){
            generateToken = generateToken.toBuilder()
                    .email(personalInformation.getEmail())
                    .cellphone(personalInformation.getCellphone()).build();
        }
        return generateToken;
    }

    public String getCellphoneByLastToken(Acquisition acquisition) {
        return generateTokenRepository.findTopByAcquisitionOrderByCreatedDateDesc(acquisition).getCellphone();
    }
}