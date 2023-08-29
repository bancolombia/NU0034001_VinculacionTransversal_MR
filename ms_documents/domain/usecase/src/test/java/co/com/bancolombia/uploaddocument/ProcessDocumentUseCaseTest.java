package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.uploaddocument.AcquisitionProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxRequest;
import co.com.bancolombia.model.uploaddocument.UploadDocumentResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.model.uploaddocument.gateways.ProcessDocumentRestRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_CEDULA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROCESS_CODE_RUT;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ProcessDocumentUseCaseTest {

    @InjectMocks
    @Spy
    private ProcessDocumentUseCaseImpl processDocumentUseCase;

    @Mock
    private ProcessDocumentRestRepository processDocumentRestRepository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ProcessDocumentSaveUseCase processDocumentSaveUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void consumeKofaxCcTest() {
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(UploadDocumentTotal.builder()
                        .uploadDocumentResponse(UploadDocumentResponse.builder().build())
                        .build())
                .build();

        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(uploadDocumentWithLog).when(processDocumentRestRepository).getRest(
                any(ProcessDocumentKofaxRequest.class), anyString(), any(Date.class));
        doNothing().when(processDocumentSaveUseCase).saveDigitalizationIdentity(
                any(UploadDocumentResponse.class), any(AcquisitionProcessDocument.class), anyString());

        ProcessDocument processDocument = ProcessDocument.builder()
                .processCode(PROCESS_CODE_CEDULA)
                .messageId("123456")
                .acquisition(AcquisitionProcessDocument.builder().build())
                .build();

        UploadDocumentWithLog response = processDocumentUseCase.consumeKofax(processDocument, "USRTRANS");
        assertNotNull(response);
    }

    @Test
    public void consumeKofaxRutTest() {
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(UploadDocumentTotal.builder()
                        .uploadDocumentResponse(UploadDocumentResponse.builder().build())
                        .build())
                .build();

        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(uploadDocumentWithLog).when(processDocumentRestRepository).getRest(
                any(ProcessDocumentKofaxRequest.class), anyString(), any(Date.class));
        doNothing().when(processDocumentSaveUseCase).saveDigitalizationRut(
                any(UploadDocumentResponse.class), any(AcquisitionProcessDocument.class), anyString());

        ProcessDocument processDocument = ProcessDocument.builder()
                .processCode(PROCESS_CODE_RUT)
                .messageId("123456")
                .acquisition(AcquisitionProcessDocument.builder().build())
                .build();

        UploadDocumentWithLog response = processDocumentUseCase.consumeKofax(processDocument, "USRTRANS");
        assertNotNull(response);
    }

    @Test
    public void consumeKofaxNullResponseTest() {
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder()
                .uploadDocumentTotal(UploadDocumentTotal.builder().build())
                .build();

        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(uploadDocumentWithLog).when(processDocumentRestRepository).getRest(
                any(ProcessDocumentKofaxRequest.class), anyString(), any(Date.class));

        ProcessDocument processDocument = ProcessDocument.builder()
                .processCode(PROCESS_CODE_CEDULA)
                .messageId("123456")
                .acquisition(AcquisitionProcessDocument.builder().build())
                .build();

        UploadDocumentWithLog response = processDocumentUseCase.consumeKofax(processDocument, "USRTRANS");
        assertNotNull(response);
    }
}
