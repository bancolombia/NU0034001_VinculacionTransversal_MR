package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.TARGET_NATURAL_PERSON;

@RequiredArgsConstructor
public class NaturalPersonUseCaseImpl implements NaturalPersonUseCase {

    private final DirectAsyncGateway directAsyncGateway;
    private final TriggerExceptionUseCase triggerExceptionUseCase;

    @Override
    public ValidateIdentityReply validateIdentity(String acquisitionId) {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(acquisitionId).build();
        AsyncQuery<AcquisitionIdQuery> asyncQuery = new AsyncQuery<>(RES_NP_VALIDATE_IDENTITY, query);
        ValidateIdentityReply reply = directAsyncGateway.requestReply(asyncQuery, TARGET_NATURAL_PERSON,
                ValidateIdentityReply.class).block();
        if (!reply.isValid()) {
            triggerExceptionUseCase.trigger(reply.getErrorList());
        }
        return reply;
    }
}
