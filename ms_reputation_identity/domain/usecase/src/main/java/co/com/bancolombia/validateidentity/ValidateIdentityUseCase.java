package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponseError;

public interface ValidateIdentityUseCase {

    ValidateIdentity startProcessValidateIdentity(AcquisitionReply acquisitionReply, BasicAcquisitionRequest bRequest);

    String getFirstSurname(ValidateIdentityReply validateReply);

    void acquisitionNotFoundValidate(AcquisitionReply acquisitionReply, ValidateIdentityResponseError vIError);
}
