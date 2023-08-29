package co.com.bancolombia.model.validateidentityrules.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;

public interface ValidateIdentityRulesRabbitRepository {
   
    ValidateIdentityRulesReply validateIdentityRules(AcquisitionIdQuery query);


}
