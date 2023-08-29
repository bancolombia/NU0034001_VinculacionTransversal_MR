package co.com.bancolombia.model.acquisition.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateCountQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateCountReply;

public interface AcquisitionRabbitRepository {
    AcquisitionReply getAndValidateAcquisition(AcquisitionQuery query);

    EmptyReply updateStateAcquisition(AcquisitionUpdateQuery query);

    EmptyReply updateAcquisition(AcquisitionIdQuery query);

    AcquisitionStateCountReply countAcquisitionByState(AcquisitionStateCountQuery query);
}