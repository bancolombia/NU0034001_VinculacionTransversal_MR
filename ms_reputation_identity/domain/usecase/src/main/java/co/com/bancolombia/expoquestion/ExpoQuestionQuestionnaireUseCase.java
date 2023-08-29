package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;

public interface ExpoQuestionQuestionnaireUseCase {
    QuestionnaireResponse startProcessQuestionnaire(AcquisitionReply acquisitionReply,
                                                    BasicAcquisitionRequest bARequest,
                                                    ValidateIdentificationResponse vIdentificationResponse,
                                                    String idExpoQuestion);

    QuestionnaireRequest createQuestionnaireRequest(AcquisitionReply acquisitionReply,
                                                    ValidateIdentificationResponse vIdentifiResponse);

    QuestionnaireResponse actionValidation(AcquisitionReply acqReply, QuestionnaireResponse questionnaireResponse);

    void valiStateAndErrors(AcquisitionReply acquisitionReply, String exception, String code, String detail);

}
