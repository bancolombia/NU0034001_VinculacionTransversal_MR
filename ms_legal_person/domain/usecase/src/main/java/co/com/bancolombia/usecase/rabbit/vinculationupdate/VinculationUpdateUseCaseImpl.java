package co.com.bancolombia.usecase.rabbit.vinculationupdate;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.usecase.util.rabbit.DependentFieldSearchInput;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_ACQUISITION_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_CHECKLIST_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_ACQUISITION;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.TARGET_VINCULATION;

@RequiredArgsConstructor
public class VinculationUpdateUseCaseImpl  implements VinculationUpdateUseCase {

    private final DirectAsyncGateway directAsyncGateway;
    private final TriggerExceptionUseCase triggerExceptionUseCase;
    private final VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo;

    public AcquisitionReply validateAcquisition(
            String acquisitionId, String documentType, String documentNumber,String operation){
        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId(acquisitionId).documentNumber(documentNumber)
                .documentType(documentType).operation(operation).build();
        AsyncQuery<AcquisitionQuery> asyncQuery = new AsyncQuery<>(RES_VIN_VALIDATE_ACQUISITION, query);
        AcquisitionReply acquisitionReply = directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, AcquisitionReply.class).block();
        if (!acquisitionReply.isValid()){
            triggerExceptionUseCase.trigger(acquisitionReply.getErrorList());
        }
        return acquisitionReply;
    }

    public EmptyReply updateAcquisition(
            String acquisitionId, String stateCode){
        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId(acquisitionId).stateCode(stateCode).build();
        AsyncQuery<AcquisitionUpdateQuery> asyncQuery = new AsyncQuery<>(RES_VIN_UPDATE_ACQUISITION_STATE, query);
        EmptyReply reply = directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, EmptyReply.class).block();
        if (!reply.isValid()){
            triggerExceptionUseCase.trigger(reply.getErrorList());
        }
        return reply;
    }

    public void markOperation(UUID acquisitionId, String stepCode, String stateCode){
        ChecklistUpdateQuery updateQuery = ChecklistUpdateQuery.builder()
                .acquisitionId(acquisitionId.toString())
                .stepCode(stepCode)
                .stateCode(stateCode).build();
        AsyncQuery<ChecklistUpdateQuery> asyncQuery = new AsyncQuery<>(RES_VIN_UPDATE_CHECKLIST_STATE, updateQuery);
        directAsyncGateway.requestReply(asyncQuery, TARGET_VINCULATION, EmptyReply.class);
    }

    public ChecklistReply checkListStatus(UUID acquisitionId, String operation){
        return vinculationUpdateUseCaseTwo.checkListStatus(acquisitionId, operation);
    }

    public EmptyReply validateCatalog(
            Map<String, List<CatalogQuery>> catalog, Map<String, List<GeographicQuery>> catalogGeo){
        return vinculationUpdateUseCaseTwo.validateCatalog(catalog, catalogGeo);
    }

    public DependentFieldReply dependentFieldNormal(
            String typeAcquisition, String currentOperation, List<String> currentFields, boolean isActive){
        return vinculationUpdateUseCaseTwo.dependentFieldNormal
                (typeAcquisition, currentOperation, currentFields, isActive);
    }

    public DependentFieldReply dependentFieldSearch(
            DependentFieldSearchInput input){
        return vinculationUpdateUseCaseTwo.dependentFieldSearch(input);
    }

    public Long countAcquisitionByState(String state, List<UUID> acquisitionIdList){
        return vinculationUpdateUseCaseTwo.countAcquisitionByState(state,acquisitionIdList);
    }

}