package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;

import java.util.Date;

public interface VinculationUpdateUseCase {
    EmptyReply updateAcquisition(
            String acquisitionId, String stateCode);

    AcquisitionReply validateAcquisition(
            String acquisitionId, String documentType, String documentNumber, String operation);

    void markOperation(String acquisitionId, String stepCode, String stateCode);

    ChecklistReply checkListStatus(String acquisitionId, String operation);

    ValidateIdentityRulesReply validateRules(String acquisitionId);

    void blockCustomer(String docType, String docNumber, Date unlockDate, String operation);
}
