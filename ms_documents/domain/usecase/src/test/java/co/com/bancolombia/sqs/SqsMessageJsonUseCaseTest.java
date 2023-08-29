package co.com.bancolombia.sqs;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class SqsMessageJsonUseCaseTest {

    @InjectMocks
    @Spy
    private SqsMessageJsonUseCaseImpl sqsMessageJsonUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void jsFilesSameDocumentTest() {
        List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(
                ProcessDocumentFiles.builder().base64("Base64")
                        .extension("application/pdf").fileName("001_TIPDOC_FS001_1000000_1.pdf").build());

        List<UploadDocumentFile> listGet = Arrays.asList(
                UploadDocumentFile.builder().fileName("001_TIPDOC_FS001_1000000_1.pdf").build());

        UploadDocumentParameter upload = UploadDocumentParameter.builder()
                .processDocumentFiles(processDocumentFiles)
                .acquisition(Acquisition.builder().id(UUID.randomUUID()).build())
                .meta(SqsMetaUploadDocument.builder().build())
                .listGet(listGet)
                .build();

        JsonArray jsonArray = sqsMessageJsonUseCase.jsFiles(upload);
        assertEquals(1, jsonArray.size());
    }

    @Test
    public void jsFilesDifferentDocumentTest() {
        List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(
                ProcessDocumentFiles.builder().base64("Base64")
                        .extension("application/pdf").fileName("001_TIPDOC_FS001_1000001_1.pdf").build());

        List<UploadDocumentFile> listGet = Arrays.asList(
                UploadDocumentFile.builder().fileName("001_TIPDOC_FS001_1000000_1.pdf").build());

        UploadDocumentParameter upload = UploadDocumentParameter.builder()
                .processDocumentFiles(processDocumentFiles)
                .acquisition(Acquisition.builder().id(UUID.randomUUID()).build())
                .meta(SqsMetaUploadDocument.builder().build())
                .listGet(listGet)
                .build();

        JsonArray jsonArray = sqsMessageJsonUseCase.jsFiles(upload);
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void dataAcquisitionTest() {
        Acquisition acquisition = Acquisition.builder()
                .id(UUID.randomUUID()).documentNumber("123456")
                .documentType(DocumentType.builder().code("FS001").build()).build();

        JsonObject jsonObject = sqsMessageJsonUseCase.dataAcquisition(acquisition, 3);
        assertNotNull(jsonObject);
    }

    @Test
    public void metaJsonTest() {
        SqsMetaUploadDocument meta = SqsMetaUploadDocument.builder().build();
        JsonObject jsonObject = sqsMessageJsonUseCase.metaJson(meta);
        assertNotNull(jsonObject);
    }

    @Test
    public void messageHeadersTest() {
        SqsMessage sqsMessage = SqsMessage.builder().id(UUID.randomUUID()).build();
        Map<String, MessageAttributeValue> headers = sqsMessageJsonUseCase.messageHeaders(sqsMessage);
        assertNotNull(headers);
    }
}
