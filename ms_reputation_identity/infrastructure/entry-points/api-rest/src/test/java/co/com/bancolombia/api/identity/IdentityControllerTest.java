package co.com.bancolombia.api.identity;

import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityUseCase;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class IdentityControllerTest {
    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private IdentityController identityController;

    @Mock
    private ValidateIdentityUseCase validateIdentityUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(identityController)
                .build();
    }

    @Test
    public void healthTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                get("/reputation-identity/api/v1/health")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(identityController);
    }

    @Test
    public void profileCustomerTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        UserInfoRequestData data = new UserInfoRequestData(uuidCode.toString(), "TIPDOC_FS001", "12345678");
        MetaRequest meta = TestUtils.buildMetaRequest("validate-identity");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep)
                .firstStepForLogFunctional(any(UserInfoRequestData.class), any(MetaRequest.class), anyString());
        Mockito.doReturn(AcquisitionReply.builder().acquisitionId("").documentNumber("").documentType("").build())
                .when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(ValidateIdentity.builder().outComeCode("").outComeName("")
                        .matchPercentaje(Double.parseDouble("0.0")).infoReuseCommon(infoReuseCommon).build())
                .when(validateIdentityUseCase).startProcessValidateIdentity(any(AcquisitionReply.class),
                        any(BasicAcquisitionRequest.class));
        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class), anyString(),
                any(InfoReuseCommon.class), anyString());

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-identity").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testAcquisitionNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData(null, "TIPDOC_FS001", "12345678");
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-identity").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDocumentNumberNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData("df3b8c71-2911-4977-a66f-d6c2e23e3310", "TIPDOC_FS001", null);
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-identity").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDocumentTypeNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData("df3b8c71-2911-4977-a66f-d6c2e23e3310", null, "12345678");
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-identity").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
