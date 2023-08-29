package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_CEDULA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_RUT;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class UploadDocumentProcessUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentProcessUseCaseImpl uploadDocumentProcessUseCase;

    @Mock
    private ProcessDocumentUseCase processDocumentUseCase;

    @Mock
    private UploadDocumentCcRulesUseCase uploadDocumentCcRulesUseCase;

    @Mock
    private UploadDocumentRutRulesUseCase uploadDocumentRutRulesUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void processDocumentCcTest() {
        UploadDocumentTotal uploadDocumentTotal = UploadDocumentTotal.builder()
                .uploadDocumentResponse(UploadDocumentResponse.builder().build()).build();
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(uploadDocumentTotal).build();
        ProcessDocumentKofaxTotal kofaxTotal = ProcessDocumentKofaxTotal.builder().build();

        doReturn(uploadDocumentWithLog).when(processDocumentUseCase).consumeKofax(
                any(ProcessDocument.class), anyString());
        doReturn(kofaxTotal).when(uploadDocumentCcRulesUseCase).validateCcDocument(
                any(UploadDocumentResponse.class), any(AcquisitionProcessDocument.class));

        ProcessDocument process = ProcessDocument.builder().processCode(PROCESS_CODE_CEDULA).build();
        UploadDocumentApiResponse apiResponse = uploadDocumentProcessUseCase.processDocument(process, "USRTRANS");
        assertNotNull(apiResponse);
    }

    @Test
    public void processDocumentRutTest() {
        UploadDocumentTotal uploadDocumentTotal = UploadDocumentTotal.builder()
                .uploadDocumentResponse(UploadDocumentResponse.builder().build()).build();
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(uploadDocumentTotal).build();
        ProcessDocumentKofaxTotal kofaxTotal = ProcessDocumentKofaxTotal.builder().build();

        doReturn(uploadDocumentWithLog).when(processDocumentUseCase).consumeKofax(
                any(ProcessDocument.class), anyString());
        doReturn(kofaxTotal).when(uploadDocumentRutRulesUseCase).validateRutDocument(
                any(UploadDocumentResponse.class), any(AcquisitionProcessDocument.class));

        ProcessDocument process = ProcessDocument.builder().processCode(PROCESS_CODE_RUT).build();
        UploadDocumentApiResponse apiResponse = uploadDocumentProcessUseCase.processDocument(process, "USRTRANS");
        assertNotNull(apiResponse);
    }

    @Test
    public void processDocumentOtherTest() {
        UploadDocumentTotal uploadDocumentTotal = UploadDocumentTotal.builder()
                .uploadDocumentResponse(UploadDocumentResponse.builder().build()).build();
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(uploadDocumentTotal).build();

        doReturn(uploadDocumentWithLog).when(processDocumentUseCase).consumeKofax(
                any(ProcessDocument.class), anyString());

        ProcessDocument process = ProcessDocument.builder().processCode("CODE").build();
        UploadDocumentApiResponse apiResponse = uploadDocumentProcessUseCase.processDocument(process, "USRTRANS");
        assertNotNull(apiResponse);
    }

    @Test
    public void processDocumentNullResponseTest() {
        UploadDocumentTotal uploadDocumentTotal = UploadDocumentTotal.builder().build();
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(uploadDocumentTotal).build();

        doReturn(uploadDocumentWithLog).when(processDocumentUseCase).consumeKofax(
                any(ProcessDocument.class), anyString());

        ProcessDocument process = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = uploadDocumentProcessUseCase.processDocument(process, "USRTRANS");
        assertNotNull(apiResponse);
    }
}
