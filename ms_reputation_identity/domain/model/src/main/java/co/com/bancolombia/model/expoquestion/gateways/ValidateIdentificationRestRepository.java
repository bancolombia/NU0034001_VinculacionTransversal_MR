package co.com.bancolombia.model.expoquestion.gateways;

import co.com.bancolombia.model.expoquestion.ValidateIdentificationRequest;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;

public interface ValidateIdentificationRestRepository {
    ValidateIdentificationResponse getUserInfoIdentification(
            ValidateIdentificationRequest identificationRequest, String messageId);
}
