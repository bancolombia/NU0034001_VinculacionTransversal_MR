package co.com.bancolombia.model.economicinformation.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.UpdateCiiuQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;

public interface EconomicRabbitRepository {
    InfoRutReply getRequiredRut(AcquisitionIdQuery query);

    EmptyReply updateCiiu(UpdateCiiuQuery query);
}
