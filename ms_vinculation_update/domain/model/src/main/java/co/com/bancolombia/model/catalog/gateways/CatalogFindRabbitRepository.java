package co.com.bancolombia.model.catalog.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;

public interface CatalogFindRabbitRepository {
    CatalogReply findCatalogCodeAndParent(CatalogQuery query);
}
