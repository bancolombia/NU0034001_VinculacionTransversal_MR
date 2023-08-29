package co.com.bancolombia.api.signdocument;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.signdocument.SDRequest;
import co.com.bancolombia.api.model.signdocument.SDRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.SignDocument;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.signdocument.SignDocumentUseCase;
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
import javax.validation.Validator;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class SignDocumentControllerTest {
    private MockMvc mvc;

    private JacksonTester<SDRequest> jsonRequest;

    @InjectMocks
    @Spy
    private SignDocumentController signDocumentController;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private SignDocumentUseCase signDocumentUseCase;

    @Mock
    private StepForLogFunctional stepForLogFunctional;

    @Mock
    GenericStep genericStep;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Validator validator;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(signDocumentController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        Acquisition acquisition = Acquisition.builder()
                .createdDate(new Date()).id(uuidCode).documentNumber("12345")
                .documentType(documentType).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(signDocumentController);
    }

    @Test
    public void signDocumentControllerTest() throws Exception {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder()
                .acquisitionId("0f14d0ab-9605-4a62-a9e4-5ed26688389").documentNumber("202011100003")
                .documentType("TIPDOC_FS001").stateCode("1").build();
        UUID uuidCode = UUID.fromString(acquisitionReply.getAcquisitionId());

        SDRequestData data = SDRequestData.builder()
                .documentCode("0").acquisitionId(uuidCode.toString()).documentNumber("202011100003")
                .documentType("TIPDOC_FS001").build();

        MetaRequest meta = TestUtils.buildMetaRequest("sign-document");
        doReturn(stepForLogFunctional).when(genericStep).firstStepForLogFunctional(any(SDRequestData.class),
                any(MetaRequest.class), anyString());

        doNothing().when(genericStep).validRequest(any(SDRequestData.class));

        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());

        SDRequest sdRequest = new SDRequest(meta, data);
        String json = jsonRequest.write(sdRequest).getJson();

        SignDocument signDocument = SignDocument.builder().file("asd").build();

        doReturn(signDocument).when(signDocumentUseCase).startProcess(
                any(Acquisition.class), any(SDRequestTxt.class));

        MockHttpServletResponse response = mvc.perform(
                        post("/documents/api/v1/sign-document")
                                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
