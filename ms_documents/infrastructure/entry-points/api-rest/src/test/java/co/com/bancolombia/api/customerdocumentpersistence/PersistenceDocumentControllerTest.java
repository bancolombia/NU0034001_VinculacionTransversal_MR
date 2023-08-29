package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequest;
import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.persistencedocument.PersistenceDocumentUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PersistenceDocumentControllerTest {
    private MockMvc mvc;

    private JacksonTester<CustomerPersistenceDocumentRequest> jsonAcquisitionRequest;

    @InjectMocks
    CustomerDocumentPersistenceController persistenceDocumentController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private PersistenceDocumentUseCase persistenceDocumentUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Mock
    private CustomerDocumentPersistenceLogController dPLogController;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(persistenceDocumentController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();

    }

    @SneakyThrows
    @Test
    public void contextLoads() {
        assertNotNull(persistenceDocumentController);
        assertNotNull(persistenceDocumentUseCase);
    }

    @SneakyThrows
    @Test
    public void persistenceDocumentTest() {
        MetaRequest meta = TestUtils.buildMetaRequest("process-document");

        UUID uuidCode = UUID.randomUUID();
        CustomerPersistenceDocumentRequestData data = CustomerPersistenceDocumentRequestData.builder()
                .documentCode("001").acquisitionId(uuidCode.toString()).documentType("TIPDOC_FS001")
                .documentNumber("111111").build();

        CustomerPersistenceDocumentRequest persistenceDocumentRequest = new CustomerPersistenceDocumentRequest(meta, data);
        String json = jsonAcquisitionRequest.write(persistenceDocumentRequest).getJson();

        Mockito.doReturn(StepForLogFunctional.builder().build()).when(genericStep).firstStepForLogFunctional(
                any(CustomerPersistenceDocumentRequestData.class), any(MetaRequest.class), anyString());
        Mockito.doNothing().when(genericStep).validRequest(any(CustomerPersistenceDocumentRequestData.class));
        Mockito.doReturn(AcquisitionReply.builder().acquisitionId(uuidCode.toString()).build())
                .when(vinculationUpdateUseCase).validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(PersistenceDocument.builder().build()).when(persistenceDocumentUseCase)
                .startProcess(anyString(), any(AcquisitionReply.class), anyString(), anyString());
        Mockito.doNothing().when(persistenceDocumentUseCase).processResponse(any(AcquisitionReply.class),
                any(PersistenceDocument.class), anyBoolean());
        Mockito.doNothing().when(dPLogController).finallyStep(any(StepForLogFunctional.class), any(AcquisitionReply.class),
                any(PersistenceDocument.class));


        MockHttpServletResponse response = mvc.perform(
                post("/documents/api/v1/customer-document-persistence")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
