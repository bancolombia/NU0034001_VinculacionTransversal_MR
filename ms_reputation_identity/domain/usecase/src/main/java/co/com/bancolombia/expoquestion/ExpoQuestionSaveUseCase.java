package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;

public interface ExpoQuestionSaveUseCase {

    String saveIdentification(AcquisitionReply acquisitionReply, BasicAcquisitionRequest bARequest,
                              ValidateIdentificationResponse vIdentificationResponse);

    void saveQuestionnaire(QuestionnaireResponse questionnaireResponse, String idExpoQuestion);
}
