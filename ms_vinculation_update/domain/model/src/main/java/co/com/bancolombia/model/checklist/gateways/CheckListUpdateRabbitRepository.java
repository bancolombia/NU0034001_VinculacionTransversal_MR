package co.com.bancolombia.model.checklist.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistUpdateQuery;

public interface CheckListUpdateRabbitRepository {
    EmptyReply updateChecklistReply (ChecklistUpdateQuery query);
}