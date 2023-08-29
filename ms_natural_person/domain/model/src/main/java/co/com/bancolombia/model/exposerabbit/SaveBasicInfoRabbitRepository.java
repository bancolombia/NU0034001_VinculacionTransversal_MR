package co.com.bancolombia.model.exposerabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SaveBasicInfoQuery;

public interface SaveBasicInfoRabbitRepository {
    EmptyReply saveResult(SaveBasicInfoQuery query);
}
