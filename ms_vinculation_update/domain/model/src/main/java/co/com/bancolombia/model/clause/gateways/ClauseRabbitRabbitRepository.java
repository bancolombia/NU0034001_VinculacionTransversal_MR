package co.com.bancolombia.model.clause.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ClauseQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;

public interface ClauseRabbitRabbitRepository {
    ClauseReply getClause(ClauseQuery query);
}
