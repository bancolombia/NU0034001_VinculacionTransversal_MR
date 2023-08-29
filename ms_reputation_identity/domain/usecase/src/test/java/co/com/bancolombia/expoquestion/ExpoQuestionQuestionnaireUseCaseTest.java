package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.expoquestion.Questionnaire;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentification;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.gateways.QuestionnaireRestRepository;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_GENERATE_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENERATE_QUESTIONS_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INSUFFICIENT_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.LAST_CONSULT_INACTIVE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ATTEMPTS_DAY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ATTEMPTS_MONTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ATTEMPTS_YEAR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ENTERED_DAY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ENTERED_MONTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ENTERED_YEAR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.UNAUTHORIZED_CONSULT_T1;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;


@RequiredArgsConstructor
public class ExpoQuestionQuestionnaireUseCaseTest {

    @InjectMocks
    @Spy
    private ExpoQuestionQuestionnaireUseCaseImpl questionnaireUC;

    @Mock
    private ApiRiskCredential apiRiskCredential;

    @Mock
    private ExpoQuestionSaveUseCase expoQuestionSUC;

    @Mock
    private CoreFunctionDate coreFD;

    @Mock
    private QuestionnaireRestRepository questionnaireRestRepository;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ExpoQuestionValidationUseCase expoQuestionUC;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createQuestionnaireRequestTest(){
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        ValidateIdentificationResponse vIdentifiResponse = ValidateIdentificationResponse.builder()
                .data(Collections.singletonList(ValidateIdentification.builder().validationIdentifier("1").build()))
                .build();
        assertNotNull(this.questionnaireUC.createQuestionnaireRequest(acquisitionReply,vIdentifiResponse));
    }

    @Test
    public void actionValidationTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(GENERATE_QUESTIONS_OK)
                        .build())).build();
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationErrorGenerateQuestionsTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(ERROR_GENERATE_QUESTIONS)
                        .build())).build();
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationInsufficientQuestionsTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(INSUFFICIENT_QUESTIONS)
                        .build())).build();
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationMaxAttemptsDayTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(MAX_ATTEMPTS_DAY)
                        .build())).build();
        doReturn("").when(expoQuestionUC).blockCustomer(any(AcquisitionReply.class), anyString());
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationMaxAttemptsMonthTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(MAX_ATTEMPTS_MONTH)
                        .build())).build();
        doReturn("").when(expoQuestionUC).blockCustomer(any(AcquisitionReply.class), anyString());
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationMaxAttemptsYearTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(MAX_ATTEMPTS_YEAR)
                        .build())).build();
        doReturn("").when(expoQuestionUC).blockCustomer(any(AcquisitionReply.class), anyString());
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationMaxEnteredDayTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(MAX_ENTERED_DAY)
                        .build())).build();
        doReturn("").when(expoQuestionUC).blockCustomer(any(AcquisitionReply.class), anyString());
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationMaxEnteredMonthTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(MAX_ENTERED_MONTH)
                        .build())).build();
        doReturn("").when(expoQuestionUC).blockCustomer(any(AcquisitionReply.class), anyString());
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationMaxEnteredYearTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(MAX_ENTERED_YEAR)
                        .build())).build();
        doReturn("").when(expoQuestionUC).blockCustomer(any(AcquisitionReply.class), anyString());
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationLastConsultInactiveTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(LAST_CONSULT_INACTIVE)
                        .build())).build();
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationUnauthorizedConsultTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult(UNAUTHORIZED_CONSULT_T1)
                        .build())).build();
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void actionValidationDefaultTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").build();
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder()
                .data(Collections.singletonList(Questionnaire.builder().questionnarieResult("").build())).build();
        Mockito.doNothing().when(questionnaireUC).valiStateAndErrors(any(AcquisitionReply.class), anyString(),
                anyString(), anyString());
        questionnaireUC.actionValidation(acquisitionReply, questionnaireResponse);
        Mockito.verify(this.questionnaireUC, Mockito.times(1))
                .actionValidation(acquisitionReply, questionnaireResponse);
    }

    @Test
    public void valiStateAndErrorsTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentTypeCodeGenericType(
                "FS001").documentNumber("12345").build();
        String exception = "";
        String code = MAX_ATTEMPTS_DAY;
        String detail = "";
        EmptyReply emptyReply = EmptyReply.builder().build();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        doReturn(emptyReply).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        Mockito.doNothing().when(expoQuestionUC).validateException(anyString(), anyString(), anyString(), anyString());
        questionnaireUC.valiStateAndErrors(acquisitionReply, exception, code, detail);
        Mockito.verify(questionnaireUC, Mockito.times(1)).valiStateAndErrors(acquisitionReply, exception, code, detail);
    }

    @Test
    public void valiStateAndErrorsSecondTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentTypeCodeGenericType(
                "FS001").documentNumber("12345").build();
        String exception = "";
        String code = ERROR_GENERATE_QUESTIONS;
        String detail = "";
        Mockito.doNothing().when(expoQuestionUC).validateException(anyString(), anyString(),
                anyString(), anyString());
        questionnaireUC.valiStateAndErrors(acquisitionReply, exception, code, detail);
        Mockito.verify(this.questionnaireUC, Mockito.times(1)).valiStateAndErrors(acquisitionReply,
                exception, code, detail);
    }

    @Test
    public void valiStateAndErrorsOkTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentTypeCodeGenericType(
                "FS001").documentNumber("12345").build();
        String exception = "";
        String code = "";
        String detail = "";
        EmptyReply emptyReply = EmptyReply.builder().build();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        doReturn(emptyReply).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        Mockito.doNothing().when(expoQuestionUC).validateException(anyString(), anyString(), anyString(), anyString());
        questionnaireUC.valiStateAndErrors(acquisitionReply, exception, code, detail);
        Mockito.verify(questionnaireUC, Mockito.times(1)).valiStateAndErrors(acquisitionReply, exception, code, detail);
    }
}
