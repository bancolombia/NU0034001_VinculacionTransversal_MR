package co.com.bancolombia.model.customercontrol.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.BlockCustomerQuery;

public interface CustomerControlRabbitRepository {
    EmptyReply blockCustomer(BlockCustomerQuery query);
}
