package co.com.bancolombia.usecase.rabbit.vinculationupdate;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateCountQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFNormalQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFSearchQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ValidateCatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateCountReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.usecase.util.rabbit.DependentFieldSearchInput;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_CHECKLIST_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_COUNT_ACQUISITION_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_DEPENDENT_NORMAL;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_DEPENDENT_SEARCH;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_CATALOG;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.TARGET_VINCULATION;

@RequiredArgsConstructor
public class VinculationUpdateUseCaseTwo {

    private final DirectAsyncGateway directAsyncGateway;
    private final TriggerExceptionUseCase triggerExceptionUseCase;

    public ChecklistReply checkListStatus(UUID acquisitionId, String operation) {
        ChecklistQuery query = ChecklistQuery.builder()
                .acquisitionId(acquisitionId.toString()).operation(operation).build();
        AsyncQuery<ChecklistQuery> asyncQuery = new AsyncQuery<>(RES_VIN_CHECKLIST_STATE, query);
        ChecklistReply checklistReply = directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, ChecklistReply.class).block();
        if (!checklistReply.isValid()){
            triggerExceptionUseCase.trigger(checklistReply.getErrorList());
        }
        return checklistReply;
    }

    public EmptyReply validateCatalog(
            Map<String, List<CatalogQuery>> catalog, Map<String, List<GeographicQuery>> catalogGeo) {
        ValidateCatalogQuery query = ValidateCatalogQuery.builder().catalog(catalog).geographic(catalogGeo).build();
        AsyncQuery<ValidateCatalogQuery> asyncQuery = new AsyncQuery<>(RES_VIN_VALIDATE_CATALOG, query);
        EmptyReply reply = directAsyncGateway.requestReply(asyncQuery, TARGET_VINCULATION, EmptyReply.class).block();
        if (!reply.isValid()) {
            triggerExceptionUseCase.trigger(reply.getErrorList());
        }
        return reply;
    }

    public DependentFieldReply dependentFieldNormal(
            String typeAcquisition, String currentOperation, List<String> currentFields, boolean isActive) {
        DependentFNormalQuery query = DependentFNormalQuery.builder().typeAcquisition(typeAcquisition)
                .currentOperation(currentOperation).currentFields(currentFields).active(isActive).build();
        AsyncQuery<DependentFNormalQuery> asyncQuery = new AsyncQuery<>(RES_VIN_DEPENDENT_NORMAL, query);
        return directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, DependentFieldReply.class).block();
    }

    public DependentFieldReply dependentFieldSearch(
            DependentFieldSearchInput input) {
        DependentFSearchQuery query = DependentFSearchQuery.builder().idAcq(input.getIdAcq())
                .table(input.getTable())
                .searchField(input.getSearchField())
                .searchValue(input.getSearchValue()).active(input.isActive()).build();
        AsyncQuery<DependentFSearchQuery> asyncQuery = new AsyncQuery<>(RES_VIN_DEPENDENT_SEARCH, query);
        return directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, DependentFieldReply.class).block();
    }


    public Long countAcquisitionByState(String state, List<UUID> acquisitionIdList){
        AcquisitionStateCountQuery query = AcquisitionStateCountQuery.builder()
                .state(state)
                .acquisitionId(acquisitionIdList)
                .build();
        AsyncQuery<AcquisitionStateCountQuery> asyncQuery = new AsyncQuery<>(RES_VIN_COUNT_ACQUISITION_STATE,query);
        AcquisitionStateCountReply reply = directAsyncGateway.
                requestReply(asyncQuery,TARGET_VINCULATION,AcquisitionStateCountReply.class).block();

        if (!reply.isValid()){
            triggerExceptionUseCase.trigger(reply.getErrorList());
        }
        return reply.getAcquisitionNumber();
    }

}
