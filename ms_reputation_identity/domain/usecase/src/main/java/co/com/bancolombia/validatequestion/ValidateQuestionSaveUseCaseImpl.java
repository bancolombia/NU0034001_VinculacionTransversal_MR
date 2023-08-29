package co.com.bancolombia.validatequestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionnaireAnswer;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerify;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;
import co.com.bancolombia.model.validatequestion.ValidateQuestionSave;
import co.com.bancolombia.model.validatequestion.VerifyAnswers;
import co.com.bancolombia.model.validatequestion.gateways.ValidateQuestionSaveRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ValidateQuestionSaveUseCaseImpl implements ValidateQuestionSaveUseCase {

    private final ValidateQuestionSaveRepository validateQuestionSaveRepository;
    private final CoreFunctionDate coreFD;

    @Override
    public void saveInfo(QuestionnaireVerifyResponse response, AcquisitionReply acquisition,
                         BasicAcquisitionRequest request) {
        validateQuestionSaveRepository.save(transQuestionnaireVerifySave(response, acquisition, request));
    }

    public List<VerifyAnswers> transQuestionnaireAnswers(List<QuestionnaireAnswer> list) {
        List<VerifyAnswers> saveList = new ArrayList<>();
        list.forEach(l -> saveList.add(VerifyAnswers.builder().correctAnswer(l.getCorrectAnswer())
                .correctQuestion(l.getCorrectQuestion()).questionId(l.getQuestionId()).build()));
        return saveList;
    }

    public ValidateQuestionSave transQuestionnaireVerifySave(QuestionnaireVerifyResponse response,
                                                             AcquisitionReply acquisition, BasicAcquisitionRequest ba) {
        QuestionnaireVerify qVerify = response.getData().get(0);
        return ValidateQuestionSave.builder().idAcquisition(acquisition.getAcquisitionId())
                .approvedMinimumQuestions(qVerify.getApprovedMinimumQuestions())
                .completedQuestions(qVerify.getCompletedQuestions()).createdBy(ba.getUserTransaction())
                .createdDate(coreFD.toFormatDate(coreFD.getDatetime())).securityCode(qVerify.getSecurityCode())
                .fullyApprovedQuestionnaire(qVerify.getFullyApprovedQuestionnaire())
                .messageId(response.getMeta().getMessageId()).questionnaireId(qVerify.getQuestionnaireId())
                .requestDate(coreFD.toFormatDate(response.getMeta().getRequestDate()))
                .validationScore(qVerify.getValidationScore()).verificationApproval(qVerify.getVerificationApproval())
                .verificationResult(qVerify.getVerificationResult())
                .verifyAnswers(transQuestionnaireAnswers(qVerify.getQuestionnaireAnswers())).build();
    }
}
