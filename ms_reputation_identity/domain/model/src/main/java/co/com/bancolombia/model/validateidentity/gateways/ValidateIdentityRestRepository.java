package co.com.bancolombia.model.validateidentity.gateways;

import co.com.bancolombia.model.validateidentity.ValidateIdentityRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityTotalResponse;

import java.util.Date;

public interface ValidateIdentityRestRepository {

    ValidateIdentityTotalResponse getUserInfoValidateIdentity(ValidateIdentityRequest validateIdentityRequest,
                                                              String messageId, Date dateRequestApi);
}
