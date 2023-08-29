package co.com.bancolombia.expoquestion;

import co.com.bancolombia.model.expoquestion.AlertQuestionnaire;
import co.com.bancolombia.model.expoquestion.Question;
import co.com.bancolombia.model.expoquestion.QuestionnaireAlert;
import co.com.bancolombia.model.expoquestion.QuestionnaireQuestion;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponseOption;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;
import co.com.bancolombia.model.expoquestion.ResponseOption;

import java.util.List;

public interface ExpoQuestionQuestionnaireTransUseCase {

    QuestionnaireAlert transQuestionnaireAlert(AlertQuestionnaire alert);

    List<QuestionnaireQuestion> transQuestionnaireQuestions(List<Question> questions);

    List<QuestionnaireResponseOption> transQuestionnaireResponseOptions(List<ResponseOption> responseOptions);

    QuestionnaireSave transQuestionnaireSave(QuestionnaireResponse questionnaireResponse);
}
