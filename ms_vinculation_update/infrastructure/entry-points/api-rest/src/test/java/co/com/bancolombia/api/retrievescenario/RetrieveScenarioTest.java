package co.com.bancolombia.api.retrievescenario;

import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.acquisition.GenericStep;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveScenarioTest {

    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private RetrieveScenarioController retrieveScenarioController;

    @Mock
    private StepForLogFunctional stepForLogFunctional;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AcquisitionValidateUseCase acquisitionValidateUseCase;

    @Mock
    private ILogFuncAcquisitionUseCase iLogFuncAcquisitionUseCase;

    @Mock
    private ILogFuncFieldUseCase iLogFuncFieldUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(retrieveScenarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(retrieveScenarioController);
    }

    @Test
    public void retrieveScenarioControllerTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        UserInfoRequestData data = new UserInfoRequestData(uuidCode.toString(), "TIPDOC_FS001",
                "1061000000");
        MetaRequest meta = TestUtils.buildMetaRequest("retrieve-scenario");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        stepForLogFunctional.firstStepForLogFunctional(Constants.CODE_RETRIEVE_SCENARIO, AcquisitionInitial.builder().
                meta(meta).data(MyDataInitial.builder().documentNumber(data.getDocumentNumber()).documentType(data.getDocumentType()).build()).build());

        Mockito.doReturn(Optional.of(Acquisition.builder().build())).when(acquisitionValidateUseCase).validateInfoSearchAndGet(anyString(),
                anyString(), anyString(), anyString());

        MockHttpServletResponse response = mvc.perform(post("/acquisition-update/api/v1/retrieve-scenario")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testAcquisitionNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData(null, "TIPDOC_FS001", "12345678");
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/acquisition-update/api/v1/retrieve-scenario").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDocumentNumberNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData("df3b8c71-2911-4977-a66f-d6c2e23e3310", "TIPDOC_FS001", null);
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/acquisition-update/api/v1/retrieve-scenario").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDocumentTypeNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData("df3b8c71-2911-4977-a66f-d6c2e23e3310", null, "123456789");
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/acquisition-update/api/v1/retrieve-scenario").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
