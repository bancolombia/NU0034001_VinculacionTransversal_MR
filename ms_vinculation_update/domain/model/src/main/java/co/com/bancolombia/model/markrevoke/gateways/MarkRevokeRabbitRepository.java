package co.com.bancolombia.model.markrevoke.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.MarkRevokeReply;

public interface MarkRevokeRabbitRepository {
    MarkRevokeReply getMarkRevoke(AcquisitionIdQuery query);
}
