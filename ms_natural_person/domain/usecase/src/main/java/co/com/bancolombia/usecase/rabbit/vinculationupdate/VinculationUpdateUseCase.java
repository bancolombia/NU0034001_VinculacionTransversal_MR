package co.com.bancolombia.usecase.rabbit.vinculationupdate;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.usecase.util.rabbit.DependentFieldSearchInput;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface VinculationUpdateUseCase {
    EmptyReply updateAcquisition(String acquisitionId, String stateCode);

    AcquisitionReply validateAcquisition(
            String acquisitionId, String documentType, String documentNumber, String operation);

    void markOperation(UUID acquisitionId, String stepCode, String stateCode);

    ChecklistReply checkListStatus(UUID acquisitionId, String operation);

    EmptyReply validateCatalog(Map<String, List<CatalogQuery>> catalog, Map<String, List<GeographicQuery>> catalogGeo);

    DependentFieldReply dependentFieldNormal(
            String typeAcquisition, String currentOperation, List<String> currentFields, boolean isActive);

    DependentFieldReply dependentFieldSearch(DependentFieldSearchInput input);

    Long countAcquisitionByState(String state, List<UUID> acquisitionIdList);
}