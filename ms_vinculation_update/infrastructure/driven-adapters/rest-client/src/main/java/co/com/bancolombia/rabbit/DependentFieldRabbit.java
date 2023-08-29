
package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFNormalQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFSearchQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRabbitRepository;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DependentFieldRabbit extends ErrorHandleRabbit implements DependentFieldRabbitRepository {

    private final DependentFieldRepository dependentFieldRepository;

    @Override
    public DependentFieldReply dependentFieldNormal(DependentFNormalQuery query) {
        return DependentFieldReply.builder().dependentFields(
                dependentFieldRepository.findByTypeAcquisitionAndCurrentOperationAndCurrentFieldInAndActive(
                        query.getTypeAcquisition(), query.getCurrentOperation(),
                        query.getCurrentFields(), query.isActive()
                )).build();
    }

    @Override
    public DependentFieldReply dependentFieldSearch(DependentFSearchQuery query) {
        return DependentFieldReply.builder().objectList(dependentFieldRepository
                .searchDependentValueFieldAndActive(query.getIdAcq(), query.getTable(),
                        query.getSearchField(), query.getSearchValue(), query.isActive()))
                .build();
    }
}