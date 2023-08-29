package co.com.bancolombia.model.geographiccatalog.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;

public interface GeographicFindRabbitRepository {
    GeographicReply findGeographicCatalog(GeographicQuery query);
}
