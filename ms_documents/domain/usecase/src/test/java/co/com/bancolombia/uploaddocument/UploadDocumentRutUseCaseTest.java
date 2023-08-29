package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessParameterGetRequest;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.uploaddocument.KofaxRutInformation;
import co.com.bancolombia.model.uploaddocument.ProcessDocument;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentErrorResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class UploadDocumentRutUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentRutUseCaseImpl uploadDocumentRutUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private UploadDocumentSaveUseCase uplDocSaveUseCase;

    @Mock
    private UploadDocumentUseCase uploadDocumentUseCase;

    @Mock
    private UploadDocumentRutSaveUseCase uploadDocumentRutSaveUseCase;

    @Mock
    private UploadDocumentExcepUseCase uploadDocumentExcepUseCase;

    @Mock
    private UploadDocumentSyncUseCase uploadDocumentSyncUseCase;

    @Mock
    private UploadDocumentProcessUseCase uploadDocumentProcessUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void processDocumentAsynchronousTest() {
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder().build();

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(true).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(uploadDocumentWithLog).when(uploadDocumentSyncUseCase).asynchronousProcess(
                any(UploadDocumentParameter.class), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test
    public void processDocumentSuccessTest() {
        ProcessDocument processDocument = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, false, true, true);

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(processDocument).when(uploadDocumentUseCase).getRequest(any(SqsMessParameterGetRequest.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uplDocSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doReturn(true).when(uploadDocumentRutSaveUseCase).validateAndSaveInformation(
                any(KofaxRutInformation.class), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentNotValidKofaxMessageIdTest() {
        ProcessDocument processDocument = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, false, true, true);

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(processDocument).when(uploadDocumentUseCase).getRequest(any(SqsMessParameterGetRequest.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(false).when(uplDocSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        doThrow(ValidationException.class).when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentNullKofaxRutInformationTest() {
        ProcessDocument processDocument = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, false, true, false);

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(processDocument).when(uploadDocumentUseCase).getRequest(any(SqsMessParameterGetRequest.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uplDocSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());

        doThrow(ValidationException.class).when(uploadDocumentExcepUseCase).validateBusinessException(
                any(ProcessDocumentKofaxError.class), any(Acquisition.class), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
    }

    @Test
    public void processDocumentNotSaveRutInfoTest() {
        ProcessDocument processDocument = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, false, true, true);

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(processDocument).when(uploadDocumentUseCase).getRequest(any(SqsMessParameterGetRequest.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uplDocSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doReturn(false).when(uploadDocumentRutSaveUseCase).validateAndSaveInformation(
                any(KofaxRutInformation.class), any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test
    public void processDocumentNullKofaxNullErrorTest() {
        ProcessDocument processDocument = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, false, false, false);

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(processDocument).when(uploadDocumentUseCase).getRequest(any(SqsMessParameterGetRequest.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uplDocSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentErrorResponseTest() {
        ProcessDocument processDocument = ProcessDocument.builder().build();
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, true, false, false);

        doReturn("123456").when(uplDocSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(processDocument).when(uploadDocumentUseCase).getRequest(any(SqsMessParameterGetRequest.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uplDocSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());

        doThrow(ValidationException.class).when(uploadDocumentExcepUseCase).validateBackEndException(
                any(UploadDocumentApiResponse.class), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter();
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentRutUseCase.processDocument(uplDocPar, obj);
    }

    private UploadDocumentParameter getUploadDocumentParameter() {
        SqsMetaUploadDocument meta = SqsMetaUploadDocument.builder().usrMod("USRMOD").build();

        return UploadDocumentParameter.builder()
                .acquisition(Acquisition.builder().id(UUID.randomUUID()).build())
                .sqsMessage(SqsMessage.builder().build())
                .usrTransaction("USERTRANS").meta(meta)
                .listGet(Arrays.asList(UploadDocumentFile.builder().build())).build();
    }

    private UploadDocumentApiResponse getUploadDocumentApiResponse(
            boolean total, boolean error, boolean kofax, boolean rut) {

        UploadDocumentWithLog docWithLog = UploadDocumentWithLog.builder().build();

        if (total) {
            UploadDocumentTotal docTotal = UploadDocumentTotal.builder().build();
            if (error) {
                UploadDocumentErrorResponse docError = UploadDocumentErrorResponse.builder().build();
                docTotal.setUploadDocumentErrorResponse(docError);
            }
            docWithLog.setUploadDocumentTotal(docTotal);
        }

        if (kofax) {
            ProcessDocumentKofaxTotal docKofax = ProcessDocumentKofaxTotal.builder()
                    .processDocumentKofaxError(ProcessDocumentKofaxError.builder().build())
                    .build();

            if (rut) {
                KofaxRutInformation rutInfo = KofaxRutInformation.builder().build();
                docKofax.setKofaxRutInformation(rutInfo);
            }
            docWithLog.setProcessDocumentKofaxTotal(docKofax);
        }

        return UploadDocumentApiResponse.builder().data(docWithLog).build();
    }
}
