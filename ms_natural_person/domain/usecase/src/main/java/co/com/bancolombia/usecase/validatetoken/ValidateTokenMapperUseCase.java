package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;

public interface ValidateTokenMapperUseCase {

    ValidateToken mapperFromValidateToken(
            ValidateToken validateToken, ValidateTokenResponse validateTokenResponse,
            BasicAcquisitionRequest request, String cellphone);
}
