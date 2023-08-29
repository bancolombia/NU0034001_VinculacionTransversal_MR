package co.com.bancolombia.api.validatelegalrep;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.validatelegalrep.ValidateLegalRep;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.validatelegalrep.ValidateLegalRepUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ValidateLegalRepControllerTest {

    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private ValidateLegalRepController validateLegalRepController;

    @Mock
    private ValidateLegalRepUseCase validateLegalRepUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(validateLegalRepController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(validateLegalRepController);
    }

    @Test
    public void validateLegalRepTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        UserInfoRequestData requestData = UserInfoRequestData.builder()
                .acquisitionId(uuidCode.toString()).documentNumber("123456789").documentType("TIPDOC_FS001").build();

        MetaRequest meta = TestUtils.buildMetaRequest("validate-legal-representative");

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, requestData);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        doReturn(StepForLogFunctional.builder().build()).when(genericStep)
                .firstStepForLogFunctional(any(UserInfoRequestData.class), any(MetaRequest.class), anyString());

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").build();

        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());

        ValidateLegalRep validateLegalRep = ValidateLegalRep.builder()
                .validationCode("0")
                .validationDescription("asd").build();

        doReturn(validateLegalRep).when(validateLegalRepUseCase).startProcessValidateLegalRep(any(Acquisition.class), anyString());

        MockHttpServletResponse response = mvc.perform(post("/legal-person/api/v1/validate-legal-representative")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}