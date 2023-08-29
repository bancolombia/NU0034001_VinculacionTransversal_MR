package co.com.bancolombia.model.dependentfield.gateways;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFNormalQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFSearchQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;

public interface DependentFieldRabbitRepository {
	DependentFieldReply dependentFieldNormal(DependentFNormalQuery dependentFNormalQuery);
	DependentFieldReply dependentFieldSearch(DependentFSearchQuery dependentFSearchQuery);
}