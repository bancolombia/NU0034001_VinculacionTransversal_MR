package co.com.bancolombia;

import co.com.bancolombia.api.model.ValidateIdentityRequest;
import co.com.bancolombia.api.model.ValidateIdentityResponse;
import co.com.bancolombia.api.model.ValidateIdentityResponseData;
import co.com.bancolombia.api.model.ValidateQuestionResponse;
import co.com.bancolombia.api.model.ValidateQuestionResponseData;
import co.com.bancolombia.api.model.ValidateQuestionsRequest;
import co.com.bancolombia.api.model.expoquestion.Answer;
import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsRequest;
import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsResponse;
import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsResponseData;
import co.com.bancolombia.api.model.expoquestion.Question;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.model.expoquestion.Questionnaire;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validatequestion.ValidateQuestion;

import java.util.ArrayList;
import java.util.List;

public class ResponseFactory {

    private ResponseFactory() {
    }

    public static ValidateIdentityResponse buildValidateIdentityResponse(ValidateIdentityRequest request,
                                                                         ValidateIdentity validateIdentity) {
        ValidateIdentityResponseData data = ValidateIdentityResponseData.builder()
                .outComeCode(validateIdentity.getOutComeCode()).outComeName(validateIdentity.getOutComeName())
                .matchPercentaje(Double.toString(validateIdentity.getMatchPercentaje())).build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return ValidateIdentityResponse.builder().data(data).meta(metaResponse).build();
    }

    public static ExpoQuestionsResponse buildExpoQuestionnaireResponse(ExpoQuestionsRequest request,
                                                                       QuestionnaireResponse responseData) {
        Questionnaire dataService = responseData.getData().get(0);
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        dataService.getQuestion().forEach(q -> questions.add(Question.builder().identifier(q.getIdentifier())
                .order(q.getOrder()).text(q.getText()).build()));
        dataService.getResponseOptions()
                .forEach(r -> answers.add(Answer.builder().identifier(r.getIdentifier()).text(r.getText()).build()));
        ExpoQuestionsResponseData data = ExpoQuestionsResponseData.builder()
                .questionnaireId(dataService.getQuestionnarieId()).answersList(answers).questionsList(questions)
                .questionnaireRecordId(dataService.getQuestionnaireRecordId()).build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return ExpoQuestionsResponse.builder().data(data).meta(metaResponse).build();
    }

    public static ValidateQuestionResponse buildValidateQuestionnaireResponse(
            ValidateQuestionsRequest request, ValidateQuestion responseData) {
        ValidateQuestionResponseData data = ValidateQuestionResponseData.builder()
                .outComeCode(responseData.getOutComeCode()).outComeName(responseData.getOutComeName()).build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return ValidateQuestionResponse.builder().data(data).meta(metaResponse).build();
    }
}
