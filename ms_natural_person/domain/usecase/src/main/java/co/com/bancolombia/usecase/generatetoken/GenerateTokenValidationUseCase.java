package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.tokenretries.TokenRetries;

public interface GenerateTokenValidationUseCase {

    void validateGenerateToken(GenerateToken generateToken, String blockMsj);

    String validateRetries(Acquisition acquisition, TokenRetries tokenRetries, String userTransaction);

    String tokenGenerationUnBlock(Acquisition acquisition, String minutesBlock, TokenRetries tokenRetries);
}
