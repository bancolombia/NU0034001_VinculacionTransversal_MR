package co.com.bancolombia.basicinformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.basicinformation.basicinfo.BasicInfoRequest;
import co.com.bancolombia.basicinformation.basicinfo.BasicInfoRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class BasicInformationTest {

    private MockMvc mvc;

    private JacksonTester<BasicInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private BasicInformationController basicInformationController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private CoreFunctionString coreFunctionString;

    @Mock
    private BasicInformationUseCase basicInformationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(basicInformationController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(basicInformationController);
    }

    @Test
    public void basicInformationTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        BasicInfoRequestData data = BasicInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").gender("M").birthCity("MEDELLIN")
                .country("CO").civilStatus("1").nationality("AD").dependants("22").educationLevel("1")
                .socialStratum("2").housingType("2").contractType("2").entryCompanyDate("2020-07-12").pep("0")
                .build();
        MetaRequest meta = TestUtils.buildMetaRequest("basic-information");

        BasicInfoRequest basicInfoRequest = new BasicInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(basicInfoRequest).getJson();

        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();

        doReturn(acquisition).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        BasicInformation basicInformation = BasicInformation.builder().acquisition(Acquisition.builder().build())
                .gender("M").birthCity("MEDELLIN")
                .country("CO").civilStatus("1").nationality("AD").dependants(22).educationLevel("1")
                .socialStratum("2").housingType("2").contractType("2")
                .entryCompanyDate(coreFunctionDate.getDatetime()).pep("0")
                .build();
        doReturn(basicInformation).when(basicInformationController)
                .constructBasicInformation(any(BasicInfoRequest.class), any(Acquisition.class));
        doReturn(basicInformation).when(basicInformationUseCase)
                .startProcessBasicInformation(any(BasicInformation.class));
        MockHttpServletResponse response = mvc.perform(post("/natural-person/api/v1/basic-information")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void constructBasicInformation(){
        MetaRequest meta = TestUtils.buildMetaRequest("basic-information");
        BasicInfoRequestData data = BasicInfoRequestData.builder().acquisitionId(UUID.randomUUID().toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").gender("M").birthCity("MEDELLIN")
                .country("CO").civilStatus("1").nationality("AD").dependants("22").educationLevel("1")
                .socialStratum("2").housingType("2").contractType("2").entryCompanyDate("2020-07-12").pep("0")
                .build();
        BasicInfoRequest basicInfoRequest = new BasicInfoRequest(meta, data);
        doReturn(1).when(coreFunctionString).stringToInteger(anyString());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(new Date()).when(coreFunctionDate).getDatetime();

        BasicInformation basicInformation = basicInformationController
                .constructBasicInformation(basicInfoRequest, Acquisition.builder().build());
        assertNotNull(basicInformation);
    }

    @Test
    public void health(){
        ResponseEntity<String> basicInformation = basicInformationController.health();
        assertNotNull(basicInformation);
    }
}