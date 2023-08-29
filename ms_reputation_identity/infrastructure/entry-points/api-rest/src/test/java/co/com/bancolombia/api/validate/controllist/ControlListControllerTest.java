package co.com.bancolombia.api.validate.controllist;

import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.controllist.ControlListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.controllist.ControlList;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ControlListControllerTest {
    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private ControlListController controller;

    @Mock
    private WebRequest webRequest;

    @Mock
    private GenericStep genericStep;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ControlListUseCase controlListUseCase;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    @Test
    public void controlListTest() throws Exception {
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
        Mockito.doReturn(ControlList.builder().validationCode("").validationName("").infoReuseCommon(infoReuseCommon).build())
                .when(controlListUseCase).startProcessControlList(any(AcquisitionReply.class),
                        any(BasicAcquisitionRequest.class));
        Mockito.doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class), anyString(),
                any(InfoReuseCommon.class), anyString());

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-control-lists").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testAcquisitionNotFoundException() throws Exception {

        UserInfoRequestData data = new UserInfoRequestData(null, "TIPDOC_FS001", "12345678");
        MetaRequest meta = TestUtils.buildMetaRequest("profile-customer");
        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-control-lists").contentType(MediaType.APPLICATION_JSON)
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

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-control-lists").contentType(MediaType.APPLICATION_JSON)
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

        MockHttpServletResponse response = mvc.perform(post("/reputation-identity/api/v1/validate-control-lists").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
