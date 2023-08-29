package co.com.bancolombia.api.identity;

import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.model.ValidateQuestionRequestDataAnswersList;
import co.com.bancolombia.api.model.ValidateQuestionsRequest;
import co.com.bancolombia.api.model.ValidateQuestionsRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionVerifyList;
import co.com.bancolombia.model.validatequestion.ValidateQuestion;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.validatequestion.ValidateQuestionUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ValidateQuestionsControllerTest {

    private MockMvc mvc;

    private JacksonTester<ValidateQuestionsRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private ValidateQuestionsController validateQuestionsController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Mock
    private ValidateQuestionUseCase validateQuestionUseCase;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(validateQuestionsController)
                .build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(validateQuestionsController);
    }

    @Test
    public void validateQuestionTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        List<ValidateQuestionRequestDataAnswersList> validateQuestionRequestDataAnswersLists = Collections
                .singletonList(ValidateQuestionRequestDataAnswersList.builder().answerId("1").questionId("1").build());
        ValidateQuestionsRequestData validateQuestionsRequestData = ValidateQuestionsRequestData.builder()
                .questionnaireId("1").questionnaireRecordId("1").acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001")
                .answerList(validateQuestionRequestDataAnswersLists).build();
        MetaRequest meta = TestUtils.buildMetaRequest("validate-questions");
        ValidateQuestionsRequest request = new ValidateQuestionsRequest(meta, validateQuestionsRequestData);
        String json = jsonUserInfoRequest.write(request).getJson();

        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep)
                .firstStepForLogFunctional(any(UserInfoRequestData.class), any(MetaRequest.class), anyString());
        Mockito.doReturn(AcquisitionReply.builder().acquisitionId("").documentNumber("").documentType("").build())
                .when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(ValidateQuestion.builder().outComeCode("").outComeName("").infoReuseCommon(infoReuseCommon).build())
                .when(validateQuestionUseCase).startProcessValidateQuestion(any(BasicAcquisitionRequest.class),
                        any(AcquisitionReply.class), any(QuestionVerifyList.class));
        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class), anyString(),
                any(InfoReuseCommon.class), anyString());

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-questions").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testAcquisitionNotFoundException() throws Exception {
        List<ValidateQuestionRequestDataAnswersList> validateQuestionRequestDataAnswersLists = Collections
                .singletonList(ValidateQuestionRequestDataAnswersList.builder().answerId("1").questionId("1").build());
        ValidateQuestionsRequestData validateQuestionsRequestData = ValidateQuestionsRequestData.builder()
                .questionnaireId("1").questionnaireRecordId("1").acquisitionId(null)
                .documentNumber("12345").documentType("TIPDOC_FS001")
                .answerList(validateQuestionRequestDataAnswersLists).build();
        MetaRequest meta = TestUtils.buildMetaRequest("validate-questions");
        ValidateQuestionsRequest request = new ValidateQuestionsRequest(meta, validateQuestionsRequestData);
        String json = jsonUserInfoRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-questions").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDocumentNumberNotFoundException() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        List<ValidateQuestionRequestDataAnswersList> validateQuestionRequestDataAnswersLists = Collections
                .singletonList(ValidateQuestionRequestDataAnswersList.builder().answerId("1").questionId("1").build());
        ValidateQuestionsRequestData validateQuestionsRequestData = ValidateQuestionsRequestData.builder()
                .questionnaireId("1").questionnaireRecordId("1").acquisitionId(uuidCode.toString())
                .documentNumber(null).documentType("TIPDOC_FS001")
                .answerList(validateQuestionRequestDataAnswersLists).build();
        MetaRequest meta = TestUtils.buildMetaRequest("validate-questions");
        ValidateQuestionsRequest request = new ValidateQuestionsRequest(meta, validateQuestionsRequestData);
        String json = jsonUserInfoRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-questions").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDocumentTypeNotFoundException() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        List<ValidateQuestionRequestDataAnswersList> validateQuestionRequestDataAnswersLists = Collections
                .singletonList(ValidateQuestionRequestDataAnswersList.builder().answerId("1").questionId("1").build());
        ValidateQuestionsRequestData validateQuestionsRequestData = ValidateQuestionsRequestData.builder()
                .questionnaireId("1").questionnaireRecordId("1").acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType(null)
                .answerList(validateQuestionRequestDataAnswersLists).build();
        MetaRequest meta = TestUtils.buildMetaRequest("validate-questions");
        ValidateQuestionsRequest request = new ValidateQuestionsRequest(meta, validateQuestionsRequestData);
        String json = jsonUserInfoRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-questions").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
