package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.gateways.ValidateSaveRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateIdentitySaveUseCaseImpl implements ValidateIdentitySaveUseCase {

    private final ValidateIdentityTransformUseCase vIdentityTUseCase;
    private final ValidateSaveRepository saveRepository;

    @Override
    public ValidateIdentitySave save(ValidateIdentityResponse response,
                                     AcquisitionReply acquisitionReply,
                                     BasicAcquisitionRequest bRequest) {
        return saveRepository.save(vIdentityTUseCase
                .transformValidateIdentitySave(response, acquisitionReply, bRequest));
    }
}
