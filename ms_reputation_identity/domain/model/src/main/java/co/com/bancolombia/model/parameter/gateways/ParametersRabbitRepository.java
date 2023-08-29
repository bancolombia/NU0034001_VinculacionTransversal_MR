package co.com.bancolombia.model.parameter.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.query.ParameterQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.ParameterReply;

public interface ParametersRabbitRepository {
    ParameterReply findByNameParent(ParameterQuery query);
}
