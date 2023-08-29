package co.com.bancolombia.validatequestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionVerifyList;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerify;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyRequest;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;
import co.com.bancolombia.model.validatequestion.ValidateQuestion;
import co.com.bancolombia.model.validatequestion.gateways.QuestionnaireVerifyRestRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ValidateQuestionUseCaseTest {

    @InjectMocks
    @Spy
    ValidateQuestionUseCaseImpl validateQuestionUseCase;

    @Mock
    QuestionnaireVerifyRestRepository qRestRepository;

    @Mock
    ValidateQuestionSaveUseCase questionSaveUseCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    CoreFunctionDate coreFunctionDate;

    @Mock
    ApiRiskCredential apiRiskCredential;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessValidateQuestionTest() {
        List<QuestionnaireVerify> data = Collections.singletonList(QuestionnaireVerify.builder().verificationApproval("true").build());
        QuestionnaireVerifyResponse questionnaireVerifyResponse = QuestionnaireVerifyResponse.builder().data(data)
                .infoReuseCommon(InfoReuseCommon.builder().build()).build();
        QuestionVerifyList request = QuestionVerifyList.builder().build();
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(questionnaireVerifyResponse).when(qRestRepository).getUserQuestionnaireVerify(any(QuestionnaireVerifyRequest.class),
                anyString(), any(Date.class));
        Mockito.doNothing().when(questionSaveUseCase).saveInfo(any(QuestionnaireVerifyResponse.class),
                any(AcquisitionReply.class), any(BasicAcquisitionRequest.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateQuestion response = this.validateQuestionUseCase.startProcessValidateQuestion(BasicAcquisitionRequest
                .builder().messageId("").build(), AcquisitionReply.builder().build(), request);
        assertNotNull(response);
    }

    @Test
    public void startProcessValidateQuestionEmptyApprovalTest() {
        List<QuestionnaireVerify> data = Collections.singletonList(QuestionnaireVerify.builder().verificationApproval("").build());
        QuestionnaireVerifyResponse questionnaireVerifyResponse = QuestionnaireVerifyResponse.builder().data(data)
                .infoReuseCommon(InfoReuseCommon.builder().build()).build();
        QuestionVerifyList request = QuestionVerifyList.builder().build();
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(questionnaireVerifyResponse).when(qRestRepository).getUserQuestionnaireVerify(any(QuestionnaireVerifyRequest.class),
                anyString(), any(Date.class));
        Mockito.doNothing().when(questionSaveUseCase).saveInfo(any(QuestionnaireVerifyResponse.class),
                any(AcquisitionReply.class), any(BasicAcquisitionRequest.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateQuestion response = this.validateQuestionUseCase.startProcessValidateQuestion(BasicAcquisitionRequest
                .builder().messageId("").build(), AcquisitionReply.builder().build(), request);
        assertNotNull(response);
    }

    @Test
    public void startProcessValidateQuestionNotApprovalTest() {
        List<QuestionnaireVerify> data = Collections.singletonList(QuestionnaireVerify.builder().verificationApproval("false").build());
        QuestionnaireVerifyResponse questionnaireVerifyResponse = QuestionnaireVerifyResponse.builder().data(data)
                .infoReuseCommon(InfoReuseCommon.builder().build()).build();
        QuestionVerifyList request = QuestionVerifyList.builder().build();
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(questionnaireVerifyResponse).when(qRestRepository).getUserQuestionnaireVerify(any(QuestionnaireVerifyRequest.class),
                anyString(), any(Date.class));
        Mockito.doNothing().when(questionSaveUseCase).saveInfo(any(QuestionnaireVerifyResponse.class),
                any(AcquisitionReply.class), any(BasicAcquisitionRequest.class));
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ValidateQuestion response = this.validateQuestionUseCase.startProcessValidateQuestion(BasicAcquisitionRequest
                .builder().messageId("").build(), AcquisitionReply.builder().build(), request);
        assertNotNull(response);
    }
}
