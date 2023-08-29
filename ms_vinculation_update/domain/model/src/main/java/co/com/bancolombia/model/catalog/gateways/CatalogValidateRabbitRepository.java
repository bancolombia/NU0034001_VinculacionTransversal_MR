package co.com.bancolombia.model.catalog.gateways;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ValidateCatalogQuery;

public interface CatalogValidateRabbitRepository {
    EmptyReply validateCatalog(ValidateCatalogQuery query);
}
