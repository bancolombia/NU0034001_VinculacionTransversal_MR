package co.com.bancolombia.api.validate.markcustomer;

import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.markcustomer.MarkCustomerUseCase;
import co.com.bancolombia.model.markcustomer.MarkCustomer;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class MarkCustomerControllerTest {
    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonRequest;

    @InjectMocks
    @Spy
    private MarkCustomerController markCustomerController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private GenericStep genericStep;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private MarkCustomerUseCase markCustomerUseCase;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(markCustomerController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(markCustomerController);
    }

    @Test
    @SneakyThrows(Exception.class)
    public void markCustomerControllerTest() {
        UUID uuidCode = UUID.randomUUID();

        UserInfoRequestData data = UserInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").build();

        MetaRequest meta = TestUtils.buildMetaRequest("mark-customer");

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonRequest.write(userInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString()).codeTypeAcquisition("1").stateCode("1").documentNumber("123").codeTypeAcquisition("123").documentTypeOrderControlList("123").documentTypeCodeGenericType("123").stateCode("1").build();

        doReturn(StepForLogFunctional.builder().build()).when(genericStep)
                .firstStepForLogFunctional(any(UserInfoRequestData.class), any(MetaRequest.class), anyString());

        doReturn(acquisitionReply).when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());

        doReturn(MarkCustomer.builder().infoReuseCommon(InfoReuseCommon.builder().build()).build()).when(markCustomerUseCase)
                .startProcessMarkOperation(any(BasicAcquisitionRequest.class), any(Acquisition.class));
        doNothing().when(genericStep).finallyStep(any(StepForLogFunctional.class), anyString(),
                any(InfoReuseCommon.class), anyString());

        MockHttpServletResponse response = mvc.perform(
                post("/reputation-identity/api/v1/mark-customer")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows(Exception.class)
    public void markCustomerControllerNullTest() {
        UUID uuidCode = UUID.randomUUID();

        UserInfoRequestData data = UserInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").build();

        MetaRequest meta = TestUtils.buildMetaRequest("mark-customer");

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonRequest.write(userInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString()).codeTypeAcquisition("1").stateCode("1").documentNumber("123").codeTypeAcquisition("123").documentTypeOrderControlList("123").documentTypeCodeGenericType("123").stateCode("1").build();

        doReturn(StepForLogFunctional.builder().build()).when(genericStep)
                .firstStepForLogFunctional(any(UserInfoRequestData.class), any(MetaRequest.class), anyString());

        doReturn(acquisitionReply).when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());

        doReturn(null).when(markCustomerUseCase)
                .startProcessMarkOperation(any(BasicAcquisitionRequest.class), any(Acquisition.class));

        MockHttpServletResponse response = mvc.perform(
                post("/reputation-identity/api/v1/mark-customer")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
