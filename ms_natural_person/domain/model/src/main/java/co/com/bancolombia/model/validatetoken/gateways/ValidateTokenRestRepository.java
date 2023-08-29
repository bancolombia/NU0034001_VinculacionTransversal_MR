package co.com.bancolombia.model.validatetoken.gateways;

import co.com.bancolombia.model.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponseWithLog;

import java.util.Date;

public interface ValidateTokenRestRepository {
    ValidateTokenResponseWithLog getUserInfoFromValidateToken(
            ValidateTokenRequest validateTokenRequest, String messageId, Date date);
}
