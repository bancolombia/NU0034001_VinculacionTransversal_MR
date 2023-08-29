package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;

public interface NaturalPersonUseCase {

    ValidateIdentityReply validateIdentity(String acquisitionId);
}
