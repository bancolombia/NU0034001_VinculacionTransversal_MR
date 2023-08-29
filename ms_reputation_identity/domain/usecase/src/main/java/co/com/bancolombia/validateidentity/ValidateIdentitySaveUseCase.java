package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;

public interface ValidateIdentitySaveUseCase {

    ValidateIdentitySave save(ValidateIdentityResponse validateIdentityResponse,
                              AcquisitionReply acquisition,
                              BasicAcquisitionRequest bAcqR);
}
