package co.com.bancolombia.config;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateCountQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.BlockCustomerQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ClauseQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFNormalQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.DependentFSearchQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ValidateCatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateCountReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.MarkRevokeReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRabbitRepository;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionStepRabbitRepository;
import co.com.bancolombia.model.catalog.gateways.CatalogFindRabbitRepository;
import co.com.bancolombia.model.catalog.gateways.CatalogValidateRabbitRepository;
import co.com.bancolombia.model.checklist.gateways.CheckListStateRabbitRepository;
import co.com.bancolombia.model.checklist.gateways.CheckListUpdateRabbitRepository;
import co.com.bancolombia.model.clause.gateways.ClauseRabbitRabbitRepository;
import co.com.bancolombia.model.customercontrol.gateways.CustomerControlRabbitRepository;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRabbitRepository;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicFindRabbitRepository;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRabbitRepository;
import co.com.bancolombia.model.validateidentityrules.gateways.ValidateIdentityRulesRabbitRepository;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_ACQUISITION_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_BLOCK_CUSTOMER;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_CHECKLIST_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_CODE_CLAUSE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_COUNT_ACQUISITION_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_DEPENDENT_NORMAL;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_DEPENDENT_SEARCH;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_FIND_CATALOG;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_FIND_CATALOG_GEOGRAPHIC;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_ACQUISITION;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_FIND_MARK_REVOKE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_ACQUISITION_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_CHECKLIST_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_ACQUISITION;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_CATALOG;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_IDENTITY_RULES;

@Configuration
@RequiredArgsConstructor
public class ListenerConfig {

    private final AcquisitionRabbitRepository acquisitionRabbitRepository;
    private final CheckListUpdateRabbitRepository checkListUpdateRabbitRepository;
    private final CheckListStateRabbitRepository checkListStateRabbitRepository;
    private final CatalogValidateRabbitRepository catalogValidateRabbitRepository;
    private final AcquisitionStepRabbitRepository acquisitionStepRabbitRepository;
    private final DependentFieldRabbitRepository dependentFieldRabbitRepository;
    private final ValidateIdentityRulesRabbitRepository validateIdentityRulesRabbitRepository;
    private final CatalogFindRabbitRepository catalogFindRabbitRepository;
    private final CustomerControlRabbitRepository customerControlRabbitRepository;
    private final GeographicFindRabbitRepository geographicFindRabbitRepository;
    private final ClauseRabbitRabbitRepository clauseRabbitRabbitRepository;
    private final MarkRevokeRabbitRepository markRevokeRabbitRepository;

    @Bean
    public HandlerRegistry handlerRegistry() {
        return HandlerRegistry.register()
                .serveQuery(RES_VIN_VALIDATE_ACQUISITION, this::validateAcquisition, AcquisitionQuery.class)
                .serveQuery(RES_VIN_UPDATE_ACQUISITION_STATE,
                        this::updateStateAcquisition, AcquisitionUpdateQuery.class)
                .serveQuery(RES_VIN_UPDATE_ACQUISITION, this:: updateAcquisition, AcquisitionIdQuery.class)
                .serveQuery(RES_VIN_VALIDATE_CATALOG, this::validateCatalog, ValidateCatalogQuery.class)
                .serveQuery(RES_VIN_UPDATE_CHECKLIST_STATE, this::updateStateStepChecklist, ChecklistUpdateQuery.class)
                .serveQuery(RES_VIN_CHECKLIST_STATE, this::stateChecklist, ChecklistQuery.class)
                .serveQuery(RES_VIN_ACQUISITION_STATE, this::stateAcquisition, AcquisitionStateQuery.class)
                .serveQuery(RES_VIN_DEPENDENT_NORMAL, this::dependentFieldNormal, DependentFNormalQuery.class)
                .serveQuery(RES_VIN_DEPENDENT_SEARCH, this::dependentFieldSearch, DependentFSearchQuery.class)
                .serveQuery(RES_VIN_ACQUISITION_STATE, this::stateAcquisition, AcquisitionStateQuery.class)
                .serveQuery(RES_VIN_VALIDATE_IDENTITY_RULES,
                        this::validateIdentityRules, AcquisitionIdQuery.class)
                .serveQuery(RES_VIN_FIND_CATALOG, this::findCatalogCodeAndName, CatalogQuery.class)
                .serveQuery(RES_VIN_BLOCK_CUSTOMER, this::blockCustomer, BlockCustomerQuery.class)
                .serveQuery(RES_VIN_COUNT_ACQUISITION_STATE,
                        this::countAcquisitionByState, AcquisitionStateCountQuery.class)
                .serveQuery(RES_VIN_FIND_CATALOG_GEOGRAPHIC, this::findGeographicCatalog, GeographicQuery.class)
                .serveQuery(RES_VIN_CODE_CLAUSE, this::getClause, ClauseQuery.class)
                .serveQuery(RES_VIN_FIND_MARK_REVOKE, this::getMarkRevoke, AcquisitionIdQuery.class);
    }

    public Mono<AcquisitionReply> validateAcquisition(AcquisitionQuery query) {
        AcquisitionReply reply = acquisitionRabbitRepository.getAndValidateAcquisition(query);
        return Mono.just(reply);
    }

    public Mono<EmptyReply> updateStateAcquisition(AcquisitionUpdateQuery query) {
        EmptyReply reply = acquisitionRabbitRepository.updateStateAcquisition(query);
        return Mono.just(reply);
    }

    public Mono<EmptyReply> updateAcquisition(AcquisitionIdQuery query) {
        EmptyReply reply = acquisitionRabbitRepository.updateAcquisition(query);
        return Mono.just(reply);
    }

    public Mono<EmptyReply> validateCatalog(ValidateCatalogQuery query) {
        EmptyReply reply = catalogValidateRabbitRepository.validateCatalog(query);
        return Mono.just(reply);
    }

    public Mono<ChecklistReply> stateChecklist(ChecklistQuery query) {
        ChecklistReply reply = checkListStateRabbitRepository.stateChecklist(query);
        return Mono.just(reply);
    }

    public Mono<EmptyReply> updateStateStepChecklist(ChecklistUpdateQuery query) {
        EmptyReply reply = checkListUpdateRabbitRepository.updateChecklistReply(query);
        return Mono.just(reply);
    }

    public Mono<AcquisitionStateReply> stateAcquisition(AcquisitionStateQuery query) {
        AcquisitionStateReply reply = acquisitionStepRabbitRepository.getAcquisitionStepState(query);
        return Mono.just(reply);
    }

    public Mono<DependentFieldReply> dependentFieldNormal(DependentFNormalQuery query) {
        return Mono.just(dependentFieldRabbitRepository.dependentFieldNormal(query));
    }

    public Mono<DependentFieldReply> dependentFieldSearch(DependentFSearchQuery query) {
        return Mono.just(dependentFieldRabbitRepository.dependentFieldSearch(query));
    }

    public Mono<ValidateIdentityRulesReply> validateIdentityRules(AcquisitionIdQuery query) {
        ValidateIdentityRulesReply reply = validateIdentityRulesRabbitRepository.validateIdentityRules(query);
        return Mono.just(reply);
    }

    public Mono<CatalogReply> findCatalogCodeAndName(CatalogQuery query) {
        CatalogReply reply = catalogFindRabbitRepository.findCatalogCodeAndParent(query);
        return Mono.just(reply);
    }

    public Mono<EmptyReply> blockCustomer(BlockCustomerQuery query) {
        EmptyReply reply = customerControlRabbitRepository.blockCustomer(query);
        return Mono.just(reply);
    }

    public Mono<AcquisitionStateCountReply> countAcquisitionByState(AcquisitionStateCountQuery query){
        AcquisitionStateCountReply reply = acquisitionRabbitRepository.countAcquisitionByState(query);
        return Mono.just(reply);
    }

    public Mono<GeographicReply> findGeographicCatalog(GeographicQuery query) {
        GeographicReply reply = geographicFindRabbitRepository.findGeographicCatalog(query);
        return Mono.just(reply);
    }

    public Mono<ClauseReply> getClause(ClauseQuery query) {
        ClauseReply reply = clauseRabbitRabbitRepository.getClause(query);
        return Mono.just(reply);
    }

    public Mono<MarkRevokeReply> getMarkRevoke(AcquisitionIdQuery query) {
        MarkRevokeReply reply = markRevokeRabbitRepository.getMarkRevoke(query);
        return Mono.just(reply);
    }
}