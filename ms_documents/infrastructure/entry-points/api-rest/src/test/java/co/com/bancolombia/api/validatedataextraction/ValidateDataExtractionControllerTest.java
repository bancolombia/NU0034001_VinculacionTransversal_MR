package co.com.bancolombia.api.validatedataextraction;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionRequest;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.validatedataextraction.ValidateDataExtractionUseCase;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ValidateDataExtractionControllerTest {
    private MockMvc mvc;

    private JacksonTester<ValidateDataExtractionRequest> jsonRequest;

    @InjectMocks
    @Spy
    private ValidateDataExtractionController validateDataExtractionController;

    @Mock
    private ValidateDataExtractionUseCase validateDataExtractionUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidateDataExtractionCoincidenceCiiuValidate ciiuValidate;

    @Mock
    private GenericStep genericStep;


    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(validateDataExtractionController)
                .build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(validateDataExtractionController);
    }

    @Test
    public void validateDataExtractionTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString()).documentNumber("")
                .codeTypeAcquisition("").documentTypeOrderControlList("").documentTypeCodeGenericType("").stateCode("")
                .build();

        ValidateDataExtractionRequestData data = ValidateDataExtractionRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .documentalSubTypeCode("001")
                .documentalTypeCode("01").build();

        MetaRequest meta = TestUtils.buildMetaRequest("val-data-extrac");

        ValidateDataExtractionRequest validateDataExtractionRequest = new ValidateDataExtractionRequest(meta, data);
        String json = jsonRequest.write(validateDataExtractionRequest).getJson();

        UploadDocumentApiResponseData documentApiResponseData = UploadDocumentApiResponseData.builder()
                .processedDocuments(new ArrayList<>()).build();

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId())).documentNumber
                        (acquisitionReply.getDocumentNumber()).typeAcquisition(TypeAcquisition.builder().code
                        (acquisitionReply.getCodeTypeAcquisition()).build()).documentType(DocumentType.builder()
                        .codeOrderControlList(acquisitionReply.getDocumentTypeOrderControlList()).codeHomologation
                                (acquisitionReply.getDocumentTypeCodeGenericType()).build())
                .stateAcquisition(StateAcquisition.builder().code(acquisitionReply.getStateCode()).build()).build();

        Mockito.doReturn(stepForLogFunctional).when(genericStep).firstStepForLogFunctional
                (any(ValidateDataExtractionRequestData.class), any(MetaRequest.class), anyString());

        Mockito.doNothing().when(genericStep).validRequest(any(Object.class));

        Mockito.doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());

        Mockito.doReturn(ValidateDataExtraction.builder().uploadDocumentApiResponseData(documentApiResponseData)
                .infoReuseCommon(InfoReuseCommon.builder().dateRequestReuse(new Date()).build()).build())
                .when(validateDataExtractionUseCase).getAnswer(
                acquisition, "01", "001", null);

        Mockito.doReturn(null).when(ciiuValidate).findCoincidenceCiiu(any(Acquisition.class),
                any(UploadDocumentApiResponseData.class));

        Mockito.doNothing().when(genericStep).finallyStep(anyString(), any(InfoReuseCommon.class),
                anyString());

        MockHttpServletResponse response = mvc.perform(
                post("/documents/api/v1/validate-data-extraction")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void validateDataExtractionNullTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().build();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString()).documentNumber("")
                .codeTypeAcquisition("").documentTypeOrderControlList("").documentTypeCodeGenericType("").stateCode("")
                .build();

        ValidateDataExtractionRequestData data = ValidateDataExtractionRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .documentalSubTypeCode("001")
                .documentalTypeCode("01").build();

        MetaRequest meta = TestUtils.buildMetaRequest("val-data-extrac");

        ValidateDataExtractionRequest validateDataExtractionRequest = new ValidateDataExtractionRequest(meta, data);
        String json = jsonRequest.write(validateDataExtractionRequest).getJson();

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId())).documentNumber
                        (acquisitionReply.getDocumentNumber()).typeAcquisition(TypeAcquisition.builder().code
                        (acquisitionReply.getCodeTypeAcquisition()).build()).documentType(DocumentType.builder()
                        .codeOrderControlList(acquisitionReply.getDocumentTypeOrderControlList()).codeHomologation
                                (acquisitionReply.getDocumentTypeCodeGenericType()).build())
                .stateAcquisition(StateAcquisition.builder().code(acquisitionReply.getStateCode()).build()).build();

        Mockito.doReturn(stepForLogFunctional).when(genericStep).firstStepForLogFunctional
                (any(ValidateDataExtractionRequestData.class), any(MetaRequest.class), anyString());

        Mockito.doNothing().when(genericStep).validRequest(any(Object.class));

        Mockito.doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());

        Mockito.doReturn(ValidateDataExtraction.builder().uploadDocumentApiResponseData(null)
                .infoReuseCommon(InfoReuseCommon.builder().dateRequestReuse(new Date()).build()).build())
                .when(validateDataExtractionUseCase).getAnswer(
                acquisition, "01", "001", null);

        Mockito.doNothing().when(genericStep).finallyStep(anyString(), any(InfoReuseCommon.class),
                anyString());

        MockHttpServletResponse response = mvc.perform(
                post("/documents/api/v1/validate-data-extraction")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


}
