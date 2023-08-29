package co.com.bancolombia.model.personalinformation.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SavePersonalInfoQuery;

public interface PersonalRabbitRepository {
    EmptyReply saveResult(SavePersonalInfoQuery query);
}
