package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.expoquestion.AlertQuestionnaire;
import co.com.bancolombia.model.expoquestion.Question;
import co.com.bancolombia.model.expoquestion.Questionnaire;
import co.com.bancolombia.model.expoquestion.QuestionnaireAlert;
import co.com.bancolombia.model.expoquestion.QuestionnaireQuestion;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponseOption;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;
import co.com.bancolombia.model.expoquestion.ResponseOption;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExpoQuestionQuestionnaireTransUseCaseImpl implements ExpoQuestionQuestionnaireTransUseCase {

    private final CoreFunctionDate coreFD;

    @Override
    public QuestionnaireSave transQuestionnaireSave(QuestionnaireResponse questionnaireResponse) {
        Questionnaire questionnaire = questionnaireResponse.getData().get(0);
        return QuestionnaireSave.builder().createdDate(coreFD.getDatetime())
                .questionnarieId(questionnaire.getQuestionnarieId())
                .requestDate(questionnaireResponse.getMeta().getRequestDate())
                .questionnaireRecordId(questionnaire.getQuestionnaireRecordId())
                .questionnarieResult(questionnaire.getQuestionnarieResult())
                .numberAttemptsYear(questionnaire.getNumberAttemptsYear())
                .numberAttemptsMonth(questionnaire.getNumberAttemptsMonth())
                .numberAttemptsDay(questionnaire.getNumberAttemptsDay())
                .excludeCustomerParameterized(questionnaire.getExcludeCustomerParameterized())
                .messageId(questionnaireResponse.getMeta().getMessageId())
                .requestDate(questionnaireResponse.getMeta().getRequestDate())
                .createdDate(coreFD.getDatetime())
                .alert(transQuestionnaireAlert(questionnaire.getAlert()))
                .question(transQuestionnaireQuestions(questionnaire.getQuestion()))
                .responseOptions(transQuestionnaireResponseOptions(questionnaire.getResponseOptions())).build();
    }

    @Override
    public QuestionnaireAlert transQuestionnaireAlert(AlertQuestionnaire alert) {
        return QuestionnaireAlert.builder().customerAlerts(alert.getCustomerAlerts())
                .responseToAlert(alert.getResponseToAlert()).alertCode(alert.getAlertCode()).build();
    }

    @Override
    public List<QuestionnaireQuestion> transQuestionnaireQuestions(List<Question> questions) {
        List<QuestionnaireQuestion> questionsList = new ArrayList<>();
        if (questions != null) {
            questions.forEach(q -> questionsList.add(QuestionnaireQuestion.builder().identifier(q.getIdentifier())
                    .text(q.getText()).order(q.getOrder()).correctAnswerId(q.getCorrectAnswerId())
                    .weight(q.getWeight()).build()));
        }
        return questionsList;
    }

    @Override
    public List<QuestionnaireResponseOption> transQuestionnaireResponseOptions(List<ResponseOption> responseOptions) {
        List<QuestionnaireResponseOption> responseOptionsList = new ArrayList<>();
        if (responseOptions != null) {
            responseOptions.forEach(r -> responseOptionsList.add(
                    QuestionnaireResponseOption.builder().identifier(r.getIdentifier()).text(r.getText()).build()));
        }
        return responseOptionsList;
    }
}
