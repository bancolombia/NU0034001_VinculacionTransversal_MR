package co.com.bancolombia.model.acquisition.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;

public interface AcquisitionStepRabbitRepository {
    AcquisitionStateReply getAcquisitionStepState(AcquisitionStateQuery query);
}
