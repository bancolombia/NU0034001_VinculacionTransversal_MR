package co.com.bancolombia.model.exposerabbit;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;

public interface GlobalServicesRabbitRepository {
    Object servicesReply(NpGlobalServicesQuery query);
}
