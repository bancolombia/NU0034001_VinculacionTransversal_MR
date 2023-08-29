package co.com.bancolombia.model.validateidentity.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;

public interface ValidateIdentityRabbitRepository {

    ValidateIdentityReply validateIdentity(AcquisitionIdQuery query);
}
