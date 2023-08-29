package co.com.bancolombia.model.exposerabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;

public interface IdentityValResultRabbitRepository {
    IdentityValResultReply getIndentityValResultReply(AcquisitionIdQuery query);
}
