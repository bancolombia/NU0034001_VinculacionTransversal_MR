package co.com.bancolombia.model.expoquestion.gateways;

import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;

public interface ExpoQuestionSaveRepository {
    ExpoQuestionSave saveIdentification(ExpoQuestionSave expoQuestionSave);

    ExpoQuestionSave saveQuestionnaire(QuestionnaireSave questionnaireSave, String idExpoQuestion);

    ExpoQuestionSave findByAcquisitionId(String acquisitionId);
}
