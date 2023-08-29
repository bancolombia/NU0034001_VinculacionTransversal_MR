package co.com.bancolombia.model.validateidentity.gateways;

import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;

public interface ValidateSaveRepository {

    ValidateIdentitySave save(ValidateIdentitySave validateIdentitySave);
}
