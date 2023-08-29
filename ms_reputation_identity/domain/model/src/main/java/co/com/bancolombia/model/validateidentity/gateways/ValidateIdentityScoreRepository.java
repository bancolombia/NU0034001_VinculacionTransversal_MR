package co.com.bancolombia.model.validateidentity.gateways;

import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;

public interface ValidateIdentityScoreRepository {

    ValidateIdentityScore save(ValidateIdentityScore validateIdentityScore);
    ValidateIdentityScore findByAcquisitionId(String acquisitionId);
}
