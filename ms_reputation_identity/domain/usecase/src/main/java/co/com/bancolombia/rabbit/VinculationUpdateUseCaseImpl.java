package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.BlockCustomerQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.ChecklistUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;

import java.util.Date;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_BLOCK_CUSTOMER;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_CHECKLIST_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_ACQUISITION_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_UPDATE_CHECKLIST_STATE;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_ACQUISITION;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_VALIDATE_IDENTITY_RULES;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.TARGET_VINCULATION;

@RequiredArgsConstructor
public class VinculationUpdateUseCaseImpl implements VinculationUpdateUseCase {

    private final DirectAsyncGateway directAsyncGateway;
    private final TriggerExceptionUseCase triggerExceptionUseCase;

    @Override
    public AcquisitionReply validateAcquisition(
            String acquisitionId, String documentType, String documentNumber, String operation) {
        AcquisitionQuery query = AcquisitionQuery.builder()
                .acquisitionId(acquisitionId).documentNumber(documentNumber)
                .documentType(documentType).operation(operation).build();
        AsyncQuery<AcquisitionQuery> asyncQuery = new AsyncQuery<>(RES_VIN_VALIDATE_ACQUISITION, query);
        AcquisitionReply acquisitionReply = directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, AcquisitionReply.class).block();
        if (!acquisitionReply.isValid()) {
            triggerExceptionUseCase.trigger(acquisitionReply.getErrorList());
        }
        return acquisitionReply;
    }

    @Override
    public EmptyReply updateAcquisition(
            String acquisitionId, String stateCode) {
        AcquisitionUpdateQuery query = AcquisitionUpdateQuery.builder()
                .acquisitionId(acquisitionId).stateCode(stateCode).build();
        AsyncQuery<AcquisitionUpdateQuery> asyncQuery = new AsyncQuery<>(RES_VIN_UPDATE_ACQUISITION_STATE, query);
        EmptyReply reply = directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, EmptyReply.class).block();
        if (!reply.isValid()) {
            triggerExceptionUseCase.trigger(reply.getErrorList());
        }
        return reply;
    }

    @Override
    public void markOperation(String acquisitionId, String stepCode, String stateCode) {
        ChecklistUpdateQuery updateQuery = ChecklistUpdateQuery.builder()
                .acquisitionId(acquisitionId)
                .stepCode(stepCode)
                .stateCode(stateCode).build();
        AsyncQuery<ChecklistUpdateQuery> asyncQuery = new AsyncQuery<>(RES_VIN_UPDATE_CHECKLIST_STATE, updateQuery);
        directAsyncGateway.requestReply(asyncQuery, TARGET_VINCULATION, EmptyReply.class);
    }

    @Override
    public ChecklistReply checkListStatus(String acquisitionId, String operation) {
        ChecklistQuery query = ChecklistQuery.builder()
                .acquisitionId(acquisitionId).operation(operation).build();
        AsyncQuery<ChecklistQuery> asyncQuery = new AsyncQuery<>(RES_VIN_CHECKLIST_STATE, query);
        ChecklistReply checklistReply = directAsyncGateway
                .requestReply(asyncQuery, TARGET_VINCULATION, ChecklistReply.class).block();
        if (!checklistReply.isValid()) {
            triggerExceptionUseCase.trigger(checklistReply.getErrorList());
        }
        return checklistReply;
    }

    @Override
    public ValidateIdentityRulesReply validateRules(String acquisitionId) {
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(acquisitionId).build();
        AsyncQuery<AcquisitionIdQuery> asyncQuery = new AsyncQuery<>(RES_VIN_VALIDATE_IDENTITY_RULES, query);
        ValidateIdentityRulesReply reply = directAsyncGateway.requestReply(asyncQuery, TARGET_VINCULATION,
                ValidateIdentityRulesReply.class).block();
        if (!reply.isValid()) {
            triggerExceptionUseCase.trigger(reply.getErrorList());
        }
        return reply;
    }

    @Override
    public void blockCustomer(String docType, String docNumber, Date unlockDate, String operation) {
        BlockCustomerQuery blockCustomerQuery = BlockCustomerQuery.builder()
                .documentType(docType).documentNumber(docNumber).operation(operation)
                .unlockDate(unlockDate).build();
        AsyncQuery<BlockCustomerQuery> asyncQuery = new AsyncQuery<>(RES_VIN_BLOCK_CUSTOMER, blockCustomerQuery);
        directAsyncGateway.requestReply(asyncQuery, TARGET_VINCULATION, EmptyReply.class);
    }
}
