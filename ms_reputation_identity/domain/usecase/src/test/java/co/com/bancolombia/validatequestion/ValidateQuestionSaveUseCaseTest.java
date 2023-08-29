package co.com.bancolombia.validatequestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.Meta;
import co.com.bancolombia.model.validatequestion.QuestionnaireAnswer;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerify;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;
import co.com.bancolombia.model.validatequestion.ValidateQuestionSave;
import co.com.bancolombia.model.validatequestion.gateways.ValidateQuestionSaveRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class ValidateQuestionSaveUseCaseTest {

    @InjectMocks
    @Spy
    ValidateQuestionSaveUseCaseImpl validateQuestionSaveUseCase;

    @Mock
    ValidateQuestionSaveRepository validateQuestionSaveRepository;

    @Mock
    CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transQuestionnaireVerifySaveTest() {
        Meta meta = Meta.builder().messageId("").requestDate(new Date()).build();
        List<QuestionnaireAnswer> answerList = Collections.singletonList(QuestionnaireAnswer.builder()
                .correctAnswer("").correctQuestion("").questionId("").build());
        List<QuestionnaireVerify> list = Collections.singletonList(QuestionnaireVerify.builder().questionnaireId("")
                .completedQuestions("").approvedMinimumQuestions("").fullyApprovedQuestionnaire("")
                .approvedMinimumQuestions("").securityCode("").validationScore("").verificationApproval("")
                .verificationResult("").questionnaireAnswers(answerList).build());
        QuestionnaireVerifyResponse questionnaireVerifyResponse = QuestionnaireVerifyResponse.builder().data(list)
                .meta(meta).build();
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId("").build();
        BasicAcquisitionRequest ba = BasicAcquisitionRequest.builder().userTransaction("").build();

        Mockito.doReturn(new Date()).when(coreFD).getDatetime();
        Mockito.doReturn("").when(coreFD).toFormatDate(any(Date.class));
        Mockito.doReturn(ValidateQuestionSave.builder().build()).when(validateQuestionSaveRepository)
                .save(any(ValidateQuestionSave.class));

        validateQuestionSaveUseCase.saveInfo(questionnaireVerifyResponse, acquisition, ba);
        Mockito.verify(this.validateQuestionSaveUseCase, Mockito.times(1))
                .saveInfo(questionnaireVerifyResponse, acquisition, ba);
    }
}
