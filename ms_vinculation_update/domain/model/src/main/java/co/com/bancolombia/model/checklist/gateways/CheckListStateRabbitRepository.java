package co.com.bancolombia.model.checklist.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;

public interface CheckListStateRabbitRepository {
    ChecklistReply stateChecklist(ChecklistQuery query);
}