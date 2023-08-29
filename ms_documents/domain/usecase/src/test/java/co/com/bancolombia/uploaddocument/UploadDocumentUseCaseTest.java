package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class UploadDocumentUseCaseTest {

    @InjectMocks
    @Spy
    private UploadDocumentUseCaseImpl uploadDocumentUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private UploadDocumentSaveUseCase uploadDocumentSaveUseCase;

    @Mock
    private UploadDocumentExcepUseCase uplDocExcepUseCase;

    @Mock
    private UploadDocumentSyncUseCase uploadDocumentSyncUseCase;

    @Mock
    private UploadDocumentValidateErrors uploadDocumentValidateErrors;

    @Mock
    private UploadDocumentProcessUseCase uploadDocumentProcessUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void processDocumentAsynchronousTest() {
        UploadDocumentWithLog uploadDocumentWithLog = UploadDocumentWithLog.builder().build();

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(true).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(uploadDocumentWithLog).when(uploadDocumentSyncUseCase).asynchronousProcess(
                any(UploadDocumentParameter.class), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test
    public void processDocumentSuccessTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, true, true, false);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateQualityFields(any(KofaxInformation.class));
        doReturn("GENERO_M").when(uploadDocumentSaveUseCase).transformKofaxGenderField(anyString());
        doNothing().when(uploadDocumentSaveUseCase).savePersonalInfo(
                any(KofaxInformation.class), any(Acquisition.class), anyString());
        doNothing().when(uploadDocumentSaveUseCase).saveBasicInfo(
                any(KofaxInformation.class), any(Acquisition.class), anyString(), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test
    public void processDocumentNullApiResponseTest() {
        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(null).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentUseCase.processDocument(uplDocPar, obj);
        assertNull(docWithLog);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentNotValidKofaxMessageIdTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, true, true, false);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(false).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        doThrow(ValidationException.class).when(uplDocExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentUseCase.processDocument(uplDocPar, obj);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentErrorResponseTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(true, false, false, false);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());

        doThrow(ValidationException.class).when(uplDocExcepUseCase).validateBackEndException(
                any(UploadDocumentApiResponse.class), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(RUT_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentUseCase.processDocument(uplDocPar, obj);
    }

    @Test
    public void processDocumentNullKofaxNullErrorTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, false, false, false);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        UploadDocumentWithLog docWithLog = uploadDocumentUseCase.processDocument(uplDocPar, obj);
        assertNotNull(docWithLog);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentNullKofaxInformationTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, true, false, true);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());

        doThrow(ValidationException.class).when(uplDocExcepUseCase).validateBusinessException(
                any(ProcessDocumentKofaxError.class), any(Acquisition.class), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentUseCase.processDocument(uplDocPar, obj);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentNotValidQualityFieldsTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, true, true, false);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doReturn(false).when(uploadDocumentSaveUseCase).validateQualityFields(any(KofaxInformation.class));

        doThrow(ValidationException.class).when(uploadDocumentValidateErrors).validateExceptionRetries(
                any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentUseCase.processDocument(uplDocPar, obj);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentEmptyGenderTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, true, true, false);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateQualityFields(any(KofaxInformation.class));
        doReturn(EMPTY).when(uploadDocumentSaveUseCase).transformKofaxGenderField(anyString());

        doThrow(ValidationException.class).when(uploadDocumentValidateErrors).validateExceptionRetries(
                any(Acquisition.class), anyString(), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentUseCase.processDocument(uplDocPar, obj);
    }

    @Test(expected = ValidationException.class)
    public void processDocumentKofaxErrorTest() {
        UploadDocumentApiResponse apiResponse = getUploadDocumentApiResponse(false, true, true, true);

        doReturn("123456").when(uploadDocumentSaveUseCase).getMessageId();
        doReturn(false).when(uploadDocumentSyncUseCase).validateAsynchronousProcess(any(List.class));
        doReturn(apiResponse).when(uploadDocumentProcessUseCase).processDocument(any(ProcessDocument.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateKofaxMessageId(any(UploadDocumentTotal.class), anyString());
        doReturn(true).when(uploadDocumentSaveUseCase).validateQualityFields(any(KofaxInformation.class));
        doReturn("GENERO_M").when(uploadDocumentSaveUseCase).transformKofaxGenderField(anyString());
        doNothing().when(uploadDocumentSaveUseCase).savePersonalInfo(
                any(KofaxInformation.class), any(Acquisition.class), anyString());
        doNothing().when(uploadDocumentSaveUseCase).saveBasicInfo(
                any(KofaxInformation.class), any(Acquisition.class), anyString(), anyString());

        doThrow(ValidationException.class).when(uplDocExcepUseCase).validateBusinessException(
                any(ProcessDocumentKofaxError.class), any(Acquisition.class), any(SqsMessageParamAllObject.class));

        UploadDocumentParameter uplDocPar = getUploadDocumentParameter(CEDULA_SUBTYPE);
        SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();

        uploadDocumentUseCase.processDocument(uplDocPar, obj);
    }

    private UploadDocumentParameter getUploadDocumentParameter(String documentSubtype) {
        SqsMetaUploadDocument meta = SqsMetaUploadDocument.builder().usrMod("USRMOD").build();

        return UploadDocumentParameter.builder()
                .acquisition(Acquisition.builder().id(UUID.randomUUID()).build())
                .sqsMessage(SqsMessage.builder().build())
                .documentSubtype(documentSubtype)
                .usrTransaction("USERTRANS").meta(meta)
                .listGet(Arrays.asList(UploadDocumentFile.builder().build())).build();
    }

    private UploadDocumentApiResponse getUploadDocumentApiResponse(
            boolean errorDoc, boolean kofaxTotal, boolean kofaxInfo, boolean kofaxError) {

        UploadDocumentWithLog docWithLog = UploadDocumentWithLog.builder().build();

        UploadDocumentTotal docTotal = UploadDocumentTotal.builder().build();
        if (errorDoc) {
            UploadDocumentErrorResponse docError = UploadDocumentErrorResponse.builder().build();
            docTotal.setUploadDocumentErrorResponse(docError);
        }
        docWithLog.setUploadDocumentTotal(docTotal);

        if (kofaxTotal) {
            ProcessDocumentKofaxTotal docKofax = ProcessDocumentKofaxTotal.builder().build();

            if (kofaxInfo) {
                KofaxInformation kInfo = KofaxInformation.builder().gender("M").build();
                docKofax.setKofaxInformation(kInfo);
            }

            if (kofaxError) {
                ProcessDocumentKofaxError kError = ProcessDocumentKofaxError.builder().build();
                docKofax.setProcessDocumentKofaxError(kError);
            }

            docWithLog.setProcessDocumentKofaxTotal(docKofax);
        }

        return UploadDocumentApiResponse.builder().data(docWithLog).build();
    }
}
