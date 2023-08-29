package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentFilesListRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequest;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.uploaddocument.ProcessedDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocument;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateUseCase;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UploadDocumentControllerTest {

    private MockMvc mvc;

    private JacksonTester<UploadDocumentRequest> jsonRequest;

    @InjectMocks
    @Spy
    private UploadDocumentController uploadDocumentController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private UploadDocumentControllerUtil uploadDocumentControllerUtil;

    @Mock
    private UploadDocumentValidateUseCase uploadDocumentValidateUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(uploadDocumentController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(uploadDocumentController);
    }

    @Test
    public void uploadDocumentProcessTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");

        UploadDocumentRequest customerPersistenceDocumentRequest = new UploadDocumentRequest(meta, data);
        String json = jsonRequest.write(customerPersistenceDocumentRequest).getJson();

        Acquisition acquisition = Acquisition.builder()
                .id(uuidCode).documentNumber("202011100003")
                .documentType(DocumentType.builder().code("TIPDOC_FS001").build()).build();

        UploadDocument uploadDocument = UploadDocument.builder()
                .processedDocument(Collections.singletonList(
                        ProcessedDocument.builder().codeAnswerDocument("00").answerDocument("Exito").build()))
                .build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder()
                .data(uploadDocument)
                .build();
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(UploadDocumentTotal.builder()
                        .uploadDocumentResponse(uploadDocumentResponse)
                        .build())
                .build();

        doReturn(acquisition).when(uploadDocumentControllerUtil).getAcquisition(any(UploadDocumentRequestData.class));
        doReturn("001").when(uploadDocumentControllerUtil).getDocumentSubtype(any(UploadDocumentRequest.class));
        doReturn(uploadDocumentWithLog).when(uploadDocumentControllerUtil).getUploadDocumentWithLog(
                any(Acquisition.class), anyList(), any(UploadDocumentRequest.class), anyList());

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/documents/api/v1/upload-document")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void uploadDocumentProcessInfoReuseCommonTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");

        UploadDocumentRequest customerPersistenceDocumentRequest = new UploadDocumentRequest(meta, data);
        String json = jsonRequest.write(customerPersistenceDocumentRequest).getJson();

        Acquisition acquisition = Acquisition.builder()
                .id(uuidCode).documentNumber("202011100003")
                .documentType(DocumentType.builder().code("TIPDOC_FS001").build()).build();

        UploadDocument uploadDocument = UploadDocument.builder()
                .processedDocument(Collections.singletonList(
                        ProcessedDocument.builder()
                                .codeAnswerDocument("00").answerDocument("Exito").reason("Reason").build()))
                .build();
        UploadDocumentResponse uploadDocumentResponse = UploadDocumentResponse.builder()
                .data(uploadDocument)
                .build();
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(UploadDocumentTotal.builder()
                        .uploadDocumentResponse(uploadDocumentResponse)
                        .build())
                .infoReuseCommon(InfoReuseCommon.builder().build())
                .build();

        doReturn(acquisition).when(uploadDocumentControllerUtil).getAcquisition(any(UploadDocumentRequestData.class));
        doReturn("001").when(uploadDocumentControllerUtil).getDocumentSubtype(any(UploadDocumentRequest.class));
        doReturn(uploadDocumentWithLog).when(uploadDocumentControllerUtil).getUploadDocumentWithLog(
                any(Acquisition.class), anyList(), any(UploadDocumentRequest.class), anyList());

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/documents/api/v1/upload-document")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void uploadDocumentWithOutResponseTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");

        UploadDocumentRequest customerPersistenceDocumentRequest = new UploadDocumentRequest(meta, data);
        String json = jsonRequest.write(customerPersistenceDocumentRequest).getJson();

        Acquisition acquisition = Acquisition.builder()
                .id(uuidCode).documentNumber("202011100003")
                .documentType(DocumentType.builder().code("TIPDOC_FS001").build()).build();

        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(UploadDocumentTotal.builder()
                        .build())
                .build();

        doReturn(acquisition).when(uploadDocumentControllerUtil).getAcquisition(any(UploadDocumentRequestData.class));
        doReturn("001").when(uploadDocumentControllerUtil).getDocumentSubtype(any(UploadDocumentRequest.class));
        doReturn(uploadDocumentWithLog).when(uploadDocumentControllerUtil).getUploadDocumentWithLog(
                any(Acquisition.class), anyList(), any(UploadDocumentRequest.class), anyList());

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/documents/api/v1/upload-document")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void uploadDocumentWithRutTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");

        UploadDocumentRequest customerPersistenceDocumentRequest = new UploadDocumentRequest(meta, data);
        String json = jsonRequest.write(customerPersistenceDocumentRequest).getJson();

        Acquisition acquisition = Acquisition.builder()
                .id(uuidCode).documentNumber("202011100003")
                .documentType(DocumentType.builder().code("TIPDOC_FS001").build()).build();

        doReturn(acquisition).when(uploadDocumentControllerUtil).getAcquisition(any(UploadDocumentRequestData.class));
        doReturn("002").when(uploadDocumentControllerUtil).getDocumentSubtype(any(UploadDocumentRequest.class));
        doReturn(false).when(uploadDocumentValidateUseCase).validateRutDataExtraction(any(Acquisition.class), any());

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/documents/api/v1/upload-document")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void uploadDocumentWithNotRequiredRutExtractionTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        UploadDocumentFilesListRequest uploadDocumentFilesListRequest = UploadDocumentFilesListRequest.builder()
                .documentalTypeCode("01")
                .documentalSubTypeCode("001")
                .flagDataExtraction("RESPUE_S")
                .fileName("001_TIPDOC_FS001_12345678.pdf")
                .flagSynchronous("RESPUE_S")
                .build();

        UploadDocumentRequestData data = UploadDocumentRequestData.builder()
                .acquisitionId(uuidCode.toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .filesList(Collections.singletonList(uploadDocumentFilesListRequest))
                .build();

        MetaRequest meta = TestUtils.buildMetaRequest("upload-document");

        UploadDocumentRequest customerPersistenceDocumentRequest = new UploadDocumentRequest(meta, data);
        String json = jsonRequest.write(customerPersistenceDocumentRequest).getJson();

        Acquisition acquisition = Acquisition.builder()
                .id(uuidCode).documentNumber("202011100003")
                .documentType(DocumentType.builder().code("TIPDOC_FS001").build()).build();

        doReturn(acquisition).when(uploadDocumentControllerUtil).getAcquisition(any(UploadDocumentRequestData.class));
        doReturn("002").when(uploadDocumentControllerUtil).getDocumentSubtype(any(UploadDocumentRequest.class));
        doReturn(true).when(uploadDocumentValidateUseCase).validateRutDataExtraction(any(Acquisition.class), any());

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/documents/api/v1/upload-document")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
