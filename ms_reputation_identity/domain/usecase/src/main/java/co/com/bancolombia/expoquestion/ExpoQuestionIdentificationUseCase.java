package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationRequest;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;

public interface ExpoQuestionIdentificationUseCase {

    QuestionnaireResponse starProcessVIdentification(AcquisitionReply acquisitionReply,
                                                     BasicAcquisitionRequest bARequest);

    ValidateIdentificationRequest createVIdentifiRequest(AcquisitionReply acqReply, ValidateIdentityReply persoInfo);

    QuestionnaireResponse actionValidation(AcquisitionReply acqReply, BasicAcquisitionRequest bARequest,
                                           ValidateIdentificationResponse vIdentifiResponse, String id);

    void validateStateAndErrors(AcquisitionReply acqReply, String exception, String code, String detail);
}
