package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.gateways.ExpoQuestionSaveRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExpoQuestionSaveUseCaseImpl implements ExpoQuestionSaveUseCase {

    private final ExpoQuestionSaveRepository saveRepository;
    private final ExpoQuestionTransUseCase expoQuestionTUC;
    private final ExpoQuestionQuestionnaireTransUseCase questionnaireTUC;

    @Override
    public String saveIdentification(AcquisitionReply acquisitionReply, BasicAcquisitionRequest bARequest,
                                     ValidateIdentificationResponse vIdentificationResponse) {
        return saveRepository.saveIdentification(expoQuestionTUC.transExpoQuestionSave(acquisitionReply, bARequest,
                vIdentificationResponse)).getId();
    }

    @Override
    public void saveQuestionnaire(QuestionnaireResponse questionnaireResponse, String idExpoQuestion) {
        saveRepository.saveQuestionnaire(questionnaireTUC.transQuestionnaireSave(questionnaireResponse),
                idExpoQuestion);
    }
}
