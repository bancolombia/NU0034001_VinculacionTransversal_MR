package co.com.bancolombia.api.exoquestion;

import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.expoquestion.ExpoQuestionsController;
import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsRequest;
import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.expoquestion.ExpoQuestionIdentificationUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.Question;
import co.com.bancolombia.model.expoquestion.Questionnaire;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ResponseOption;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
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
public class ExpoQuestionsControllerTest {

    private MockMvc mvc;

    private JacksonTester<ExpoQuestionsRequest> jsonRequest;

    @InjectMocks
    @Spy
    private ExpoQuestionsController expoQuestionsController;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Mock
    private ExpoQuestionIdentificationUseCase vIdentificationUC;

    @Mock
    private HttpServletRequest request;

    @Mock
    private WebRequest webRequest;

    @Mock
    private CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(expoQuestionsController)
                .build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(expoQuestionsController);
    }

    @Test
    public void testExpoQuestionsController() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        MetaRequest meta = TestUtils.buildMetaRequest("expo-questions");
        ExpoQuestionsRequestData data = ExpoQuestionsRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").names("Prueba unitaria")
                .firstSurname("Prueba").secondSurname("Apellido").build();
        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep)
                .firstStepForLogFunctional(any(UserInfoRequestData.class), any(MetaRequest.class), anyString());
        ExpoQuestionsRequest expoQuestionsRequest = new ExpoQuestionsRequest(meta, data);
        String jsonExpo = jsonRequest.write(expoQuestionsRequest).getJson();
        AcquisitionReply acquisitionReply =
                AcquisitionReply.builder().acquisitionId("").documentNumber("12345").documentTypeCodeGenericType(
                        "FS001").stateCode("1").build();
        List<Question> question =
                Collections.singletonList(Question.builder().identifier("1").order("1").text("1").build());
        List<ResponseOption> responseOptions =
                Collections.singletonList(ResponseOption.builder().identifier("1").text("1").build());
        List<Questionnaire> questionnaires = Collections.singletonList(Questionnaire.builder().questionnarieId("1")
                .questionnaireRecordId("1").question(question).responseOptions(responseOptions).build());
        QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder().data(questionnaires).build();
        Mockito.doReturn(acquisitionReply).when(vinculationUpdateUseCase).validateAcquisition(anyString(),
                anyString(), anyString(), anyString());
        Mockito.doReturn(questionnaireResponse).when(vIdentificationUC)
                .starProcessVIdentification(any(AcquisitionReply.class), any(BasicAcquisitionRequest.class));
        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/expo-questions")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonExpo)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
