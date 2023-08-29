package co.com.bancolombia.model.exposerabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SendFormReply;

public interface SendFormRabbitRepository {
    SendFormReply sendFormReply(AcquisitionIdQuery query);
}
