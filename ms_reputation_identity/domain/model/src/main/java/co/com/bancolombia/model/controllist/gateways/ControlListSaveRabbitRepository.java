package co.com.bancolombia.model.controllist.gateways;

import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.query.ControlListSaveQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.ControlListSaveReply;

public interface ControlListSaveRabbitRepository {
    ControlListSaveReply findStateValidationCustomerControlList(ControlListSaveQuery query);
}
