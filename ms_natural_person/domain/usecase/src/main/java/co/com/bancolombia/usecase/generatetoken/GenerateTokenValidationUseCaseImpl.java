package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_BLOQUEADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_CONTROLADO_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_G_TOKEN_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_GEN_TOKEN_MAX_RETRIES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_GEN_TOKEN_TIME_BLOCK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_EMAIL_CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_RETRIES_GEN_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers.ONE;

@RequiredArgsConstructor
public class GenerateTokenValidationUseCaseImpl implements GenerateTokenValidationUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final Exceptions exceptions;
    private final ParametersRepository parametersRepository;
    private final CoreFunctionDate coreFunctionDate;
    private final TokenRetriesUseCase tokenRetriesUseCase;

    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_IDENTITY, OPER_G_TOKEN_OPERATION);

    public void validateGenerateToken(GenerateToken generateToken, String blockMsj){
        if (blockMsj!=null){
            adapter.info(ERROR_CONTROLADO_APP);
            exceptions.createException(null, blockMsj, Constants.EMPTY, ERROR_CODE_RETRIES_GEN_TOKEN);
        }else{
            HashMap<String, String> hashMap = new HashMap<>();
            if (generateToken.getEmail()==null){
                hashMap.put("email", Constants.EMPTY);
            }
            if (generateToken.getCellphone()==null){
                hashMap.put("cellphone", Constants.EMPTY);
            }
            if (!hashMap.isEmpty()) {
                exceptions.createException(hashMap, null, null, ERROR_CODE_EMAIL_CELLPHONE);
                adapter.info(ERROR_CONTROLADO_APP);
            }
        }
    }

    public String validateRetries(Acquisition acquisition, TokenRetries tokenRetries, String userTransaction) {
        String blockMsj = null;
        List<Parameters> maxRetries = parametersRepository.findByParent(PARAMETER_GEN_TOKEN_MAX_RETRIES);
        int retries = tokenRetries.getGenerateTokenRetries()+1;
        if (retries > Integer.parseInt(maxRetries.get(0).getCode())) {
            ChecklistReply checklist = vinculationUpdateUseCase.checkListStatus
                    (acquisition.getId(), Constants.CODE_GENERATE_TOKEN);
            String minutesBlock = parametersRepository.findByParent(PARAMETER_GEN_TOKEN_TIME_BLOCK).get(0).getCode();
            if (CODE_ST_OPE_BLOQUEADO.equals(checklist.getStateOperation())) {
                blockMsj = tokenGenerationUnBlock(acquisition, minutesBlock, tokenRetries);
            }else {
                blockMsj = coreFunctionDate.minutesFormat(Integer.parseInt(minutesBlock));
                vinculationUpdateUseCase.markOperation
                        (acquisition.getId(), Constants.CODE_GENERATE_TOKEN, Constants.CODE_ST_OPE_BLOQUEADO);
                tokenRetries.setGenerateTokenLockDate(coreFunctionDate.getDatetime());
            }
        }else {
            tokenRetries.setGenerateTokenRetries(retries);
        }
        tokenRetries.setUpdatedBy(userTransaction);
        tokenRetries.setUpdatedDate(coreFunctionDate.getDatetime());
        tokenRetriesUseCase.save(tokenRetries);
        return blockMsj;
    }

    public String tokenGenerationUnBlock(Acquisition acquisition, String minutesBlock, TokenRetries tokenRetries){
        String difference = coreFunctionDate.compareDifferenceTime(tokenRetries.getGenerateTokenLockDate(),
                minutesBlock,true,true);
        if (difference==null){
            vinculationUpdateUseCase.markOperation
                    (acquisition.getId(), Constants.CODE_GENERATE_TOKEN, Constants.CODE_ST_OPE_PENDIENTE);
            tokenRetries.setGenerateTokenRetries(ONE.getIntNumber());
        }
        return difference;
    }
}
