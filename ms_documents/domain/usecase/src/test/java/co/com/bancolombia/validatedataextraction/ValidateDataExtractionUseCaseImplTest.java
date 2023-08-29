package co.com.bancolombia.validatedataextraction;

import co.com.bancolombia.asyncdigitalization.AsyncDigitalizationUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentExcepUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ValidateDataExtractionUseCaseImplTest {

    @InjectMocks
    @Spy
    private ValidateDataExtractionUseCaseImpl validateDataExtractionUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private AsyncDigitalizationUseCase asyncDigitalizationUseCase;

    @Mock
    private SqsMessageUseCase sqsMessageUseCase;

    @Mock
    private UploadDocumentExcepUseCase uploadDocumentExcepUseCase;

    @Mock
    private ValidateDataExtractionLogUseCase validateDataExtractionLogUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAnswerTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).uploadDocumentRetries(1).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        Optional<SqsMessage> sqsMessage = Optional.ofNullable(
                SqsMessage.builder().id(UUID.randomUUID()).statusMessage("COMPLETADO").build());

        Optional<AsyncDigitalization> asyncDig = Optional.ofNullable(
                AsyncDigitalization.builder()
                        .dataResponse("{\"data\":{\"acquisition\":{\"documentNumber\":\"1088343657\",\"documentType\":\"TIPDOC_FS001\",\"id\":\"505526b5-42e5-464c-ae1f-6da25f82ce91\",\"uploadDocumentRetries\":0},\"files\":[{\"base64\":\"\",\"extension\":\"application/pdf\",\"fileName\":\"001_TIPDOC_FS001_1088343657.pdf\",\"flagDataExtraction\":\"RESPUE_S\",\"flagSyncronized\":\"RESPUE_N\",\"documentalSubTypeCode\":\"001\",\"documentalTypeCode\":\"01\"}],\"messageId\":\"13042021091751599\",\"processCode\":\"001\",\"processName\":\"VinculacionTransversal\",\"userCode\":0},\"meta\":{\"usrMod\":\"LMH_NIGHT\",\"ip\":\"ip\",\"systemId\":\"AW78461\",\"messageId\":\"3a15584b-6a7a-4ba2-8d27-3fea93c97b8f\",\"version\":\"1\",\"requestDate\":\"20150625200000\",\"service\":\"digitalization\",\"operation\":\"upload-document\"}}")
                        .build());

        ValidateDataExtraction validateDataExtraction = ValidateDataExtraction.builder().build();

        doReturn(sqsMessage).when(validateDataExtractionUseCase).findValidMessage(
                any(Acquisition.class), anyString(), anyString());
        doReturn(asyncDig).when(asyncDigitalizationUseCase).findBySqsMessage(any(SqsMessage.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doReturn(validateDataExtraction).when(validateDataExtractionLogUseCase).getObjectValid(
                any(AsyncDigitalization.class), any(UploadDocumentApiResponseData.class));

        ValidateDataExtraction res = validateDataExtractionUseCase.getAnswer(
                acquisition, documentalTypeCode, documentalSubTypeCode, null);
        assertNotNull(res);
    }

    @Test
    public void getAnswerEmptyDataResponseTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).uploadDocumentRetries(1).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        Optional<SqsMessage> sqsMessage = Optional.ofNullable(
                SqsMessage.builder().id(UUID.randomUUID()).statusMessage("COMPLETADO").build());

        Optional<AsyncDigitalization> asyncDig = Optional.ofNullable(
                AsyncDigitalization.builder()
                        .dataResponse("")
                        .errorResponse("{\"code\":\"BVNT048\",\"detail\":\"CC\",\"complement\":\" el complemento\"}")
                        .build());

        ValidateDataExtraction validateDataExtraction = ValidateDataExtraction.builder().build();

        doReturn(sqsMessage).when(validateDataExtractionUseCase).findValidMessage(
                any(Acquisition.class), anyString(), anyString());
        doReturn(asyncDig).when(asyncDigitalizationUseCase).findBySqsMessage(any(SqsMessage.class));
        doNothing().when(validateDataExtractionLogUseCase).saveInfoLog(any(AsyncDigitalization.class));
        doNothing().when(validateDataExtractionUseCase).validateBusinessException(
                any(ProcessDocumentKofaxError.class), any(Acquisition.class), any(SqsMessageParamAllObject.class));

        ValidateDataExtraction res = validateDataExtractionUseCase.getAnswer(
                acquisition, documentalTypeCode, documentalSubTypeCode, null);
        assertNull(res);
    }

    @Test
    public void getAnswerErrorTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        Optional<SqsMessage> sqsMessage = Optional.ofNullable(
                SqsMessage.builder().id(UUID.randomUUID()).statusMessage("COMPLETADO").build());

        Optional<AsyncDigitalization> asyncDig = Optional.ofNullable(
                AsyncDigitalization.builder()
                        .dataResponse(null)
                        .errorResponse("{\"code\":\"BVNT048\",\"detail\":\"CC\",\"complement\":\" el complemento\"}")
                        .build());

        doReturn(sqsMessage).when(validateDataExtractionUseCase).findValidMessage(
                any(Acquisition.class), anyString(), anyString());
        doReturn(asyncDig).when(asyncDigitalizationUseCase).findBySqsMessage(any(SqsMessage.class));
        doNothing().when(validateDataExtractionLogUseCase).saveInfoLog(any(AsyncDigitalization.class));
        doNothing().when(validateDataExtractionUseCase).validateBusinessException(
                any(ProcessDocumentKofaxError.class), any(Acquisition.class), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
        verify(validateDataExtractionUseCase).getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
    }

    @Test
    public void getAnswerExceptionTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        Optional<SqsMessage> sqsMessage = Optional.ofNullable(
                SqsMessage.builder().id(UUID.randomUUID()).statusMessage("PENDIENTE").build());

        doReturn(sqsMessage).when(validateDataExtractionUseCase).findValidMessage(
                any(Acquisition.class), anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
        verify(validateDataExtractionUseCase).getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
    }

    @Test
    public void getAnswerExceptionNoValidTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        Optional<SqsMessage> sqsMessage = Optional.ofNullable(null);

        doReturn(sqsMessage).when(validateDataExtractionUseCase).findValidMessage(
                any(Acquisition.class), anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
        verify(validateDataExtractionUseCase).getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
    }

    @Test
    public void getAnswerEmptyAsyncTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        Optional<SqsMessage> sqsMessage = Optional.ofNullable(
                SqsMessage.builder().id(UUID.randomUUID()).statusMessage("COMPLETADO").build());

        doReturn(sqsMessage).when(validateDataExtractionUseCase).findValidMessage(
                any(Acquisition.class), anyString(), anyString());
        doReturn(Optional.empty()).when(asyncDigitalizationUseCase).findBySqsMessage(any(SqsMessage.class));

        ValidateDataExtraction res = validateDataExtractionUseCase.getAnswer(acquisition, documentalTypeCode, documentalSubTypeCode, null);
        assertNull(res);
    }

    @Test
    public void findValidMessageType01Subtype001Test() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        List<SqsMessage> sqsMessageList = Arrays.asList(
                SqsMessage.builder().message("{\"data\":{\"acquisition\":{\"documentNumber\":\"1088343657\",\"documentType\":\"TIPDOC_FS001\",\"id\":\"39e40676-c90e-45de-8f45-5d9cb4208882\",\"uploadDocumentRetries\":0},\"files\":[{\"base64\":\"item.getBase64()\",\"extension\":\"application/pdf\",\"fileName\":\"001_TIPDOC_FS001_1088343657.pdf\",\"flagDataExtraction\":\"RESPUE_S\",\"flagSyncronized\":\"RESPUE_S\",\"documentalSubTypeCode\":\"001\",\"documentalTypeCode\":\"01\"}],\"messageId\":\"07042021205311810\",\"processCode\":\"PROC_Rut_VinculacionTransversal\",\"processName\":\"VinculacionTransversal\",\"userCode\":0},\"meta\":{\"usrMod\":\"BIZAGI\",\"ip\":\"ip\",\"systemId\":\"AW78461\",\"messageId\":\"3a15584b-6a7a-4ba2-8d27-3fea93c97b8s\",\"version\":\"1\",\"requestDate\":\"20150625200000\",\"service\":\"management\",\"operation\":\"start-acquisition\"}}").build());

        doReturn(sqsMessageList).when(sqsMessageUseCase).findMessageList(anyString(), any(Acquisition.class));

        Optional<SqsMessage> res = validateDataExtractionUseCase.findValidMessage(
                acquisition, documentalTypeCode, documentalSubTypeCode);
        assertNotNull(res);
    }

    @Test
    public void findValidMessageType01Subtype002Test() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        List<SqsMessage> sqsMessageList = Arrays.asList(
                SqsMessage.builder().message("{\"data\":{\"acquisition\":{\"documentNumber\":\"1088343657\",\"documentType\":\"TIPDOC_FS001\",\"id\":\"39e40676-c90e-45de-8f45-5d9cb4208882\",\"uploadDocumentRetries\":0},\"files\":[{\"base64\":\"item.getBase64()\",\"extension\":\"application/pdf\",\"fileName\":\"001_TIPDOC_FS001_1088343657.pdf\",\"flagDataExtraction\":\"RESPUE_S\",\"flagSyncronized\":\"RESPUE_S\",\"documentalSubTypeCode\":\"002\",\"documentalTypeCode\":\"01\"}],\"messageId\":\"07042021205311810\",\"processCode\":\"PROC_Rut_VinculacionTransversal\",\"processName\":\"VinculacionTransversal\",\"userCode\":0},\"meta\":{\"usrMod\":\"BIZAGI\",\"ip\":\"ip\",\"systemId\":\"AW78461\",\"messageId\":\"3a15584b-6a7a-4ba2-8d27-3fea93c97b8s\",\"version\":\"1\",\"requestDate\":\"20150625200000\",\"service\":\"management\",\"operation\":\"start-acquisition\"}}").build());

        doReturn(sqsMessageList).when(sqsMessageUseCase).findMessageList(anyString(), any(Acquisition.class));

        Optional<SqsMessage> res = validateDataExtractionUseCase.findValidMessage(
                acquisition, documentalTypeCode, documentalSubTypeCode);
        assertNotNull(res);
    }

    @Test
    public void findValidMessageType02Subtype001Test() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        List<SqsMessage> sqsMessageList = Arrays.asList(
                SqsMessage.builder().message("{\"data\":{\"acquisition\":{\"documentNumber\":\"1088343657\",\"documentType\":\"TIPDOC_FS001\",\"id\":\"39e40676-c90e-45de-8f45-5d9cb4208882\",\"uploadDocumentRetries\":0},\"files\":[{\"base64\":\"item.getBase64()\",\"extension\":\"application/pdf\",\"fileName\":\"001_TIPDOC_FS001_1088343657.pdf\",\"flagDataExtraction\":\"RESPUE_S\",\"flagSyncronized\":\"RESPUE_S\",\"documentalSubTypeCode\":\"001\",\"documentalTypeCode\":\"02\"}],\"messageId\":\"07042021205311810\",\"processCode\":\"PROC_Rut_VinculacionTransversal\",\"processName\":\"VinculacionTransversal\",\"userCode\":0},\"meta\":{\"usrMod\":\"BIZAGI\",\"ip\":\"ip\",\"systemId\":\"AW78461\",\"messageId\":\"3a15584b-6a7a-4ba2-8d27-3fea93c97b8s\",\"version\":\"1\",\"requestDate\":\"20150625200000\",\"service\":\"management\",\"operation\":\"start-acquisition\"}}").build());

        doReturn(sqsMessageList).when(sqsMessageUseCase).findMessageList(anyString(), any(Acquisition.class));

        Optional<SqsMessage> res = validateDataExtractionUseCase.findValidMessage(
                acquisition, documentalTypeCode, documentalSubTypeCode);
        assertNotNull(res);
    }

    @Test
    public void findValidMessageType02Subtype002Test() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        List<SqsMessage> sqsMessageList = Arrays.asList(
                SqsMessage.builder().message("{\"data\":{\"acquisition\":{\"documentNumber\":\"1088343657\",\"documentType\":\"TIPDOC_FS001\",\"id\":\"39e40676-c90e-45de-8f45-5d9cb4208882\",\"uploadDocumentRetries\":0},\"files\":[{\"base64\":\"item.getBase64()\",\"extension\":\"application/pdf\",\"fileName\":\"001_TIPDOC_FS001_1088343657.pdf\",\"flagDataExtraction\":\"RESPUE_S\",\"flagSyncronized\":\"RESPUE_S\",\"documentalSubTypeCode\":\"002\",\"documentalTypeCode\":\"02\"}],\"messageId\":\"07042021205311810\",\"processCode\":\"PROC_Rut_VinculacionTransversal\",\"processName\":\"VinculacionTransversal\",\"userCode\":0},\"meta\":{\"usrMod\":\"BIZAGI\",\"ip\":\"ip\",\"systemId\":\"AW78461\",\"messageId\":\"3a15584b-6a7a-4ba2-8d27-3fea93c97b8s\",\"version\":\"1\",\"requestDate\":\"20150625200000\",\"service\":\"management\",\"operation\":\"start-acquisition\"}}").build());

        doReturn(sqsMessageList).when(sqsMessageUseCase).findMessageList(anyString(), any(Acquisition.class));

        Optional<SqsMessage> res = validateDataExtractionUseCase.findValidMessage(
                acquisition, documentalTypeCode, documentalSubTypeCode);
        assertNotNull(res);
    }

    @Test
    public void findValidMessageEmptyTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        List<SqsMessage> sqsMessageList = Arrays.asList(SqsMessage.builder().message(null).build());

        doReturn(sqsMessageList).when(sqsMessageUseCase).findMessageList(anyString(), any(Acquisition.class));

        Optional<SqsMessage> res = validateDataExtractionUseCase.findValidMessage(
                acquisition, documentalTypeCode, documentalSubTypeCode);
        assertNotNull(res);
    }

    @Test
    public void findValidMessageEmptyListTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        String documentalTypeCode = "01";
        String documentalSubTypeCode = "001";

        List<SqsMessage> sqsMessageList = new ArrayList<>();

        doReturn(sqsMessageList).when(sqsMessageUseCase).findMessageList(anyString(), any(Acquisition.class));

        Optional<SqsMessage> res = validateDataExtractionUseCase.findValidMessage(
                acquisition, documentalTypeCode, documentalSubTypeCode);
        assertNotNull(res);
    }

    @Test
    public void validateBusinessExceptionTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                .code(ERROR_CODE_UPLOAD_DOCUMENT_RETRY)
                .complement("")
                .detail("")
                .build();

        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.validateBusinessException(processDocumentKofaxError, acquisition, null);
        verify(validateDataExtractionUseCase).validateBusinessException(processDocumentKofaxError, acquisition, null);
    }

    @Test
    public void validateBusinessExceptionManualTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                .code(ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL)
                .complement("")
                .detail("")
                .build();

        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.validateBusinessException(processDocumentKofaxError, acquisition, null);
        verify(validateDataExtractionUseCase).validateBusinessException(processDocumentKofaxError, acquisition, null);
    }

    @Test
    public void validateBusinessExceptionDateTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                .code(ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL)
                .complement("")
                .detail("")
                .build();

        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.validateBusinessException(processDocumentKofaxError, acquisition, null);
        verify(validateDataExtractionUseCase).validateBusinessException(processDocumentKofaxError, acquisition, null);
    }

    @Test
    public void validateBusinessExceptionOtherTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();

        ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
                .code("BVNT001")
                .complement("")
                .detail("")
                .build();

        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(uploadDocumentExcepUseCase).validateException(
                anyString(), anyString(), anyString(), any(SqsMessageParamAllObject.class));

        validateDataExtractionUseCase.validateBusinessException(processDocumentKofaxError, acquisition, null);
        verify(validateDataExtractionUseCase).validateBusinessException(processDocumentKofaxError, acquisition, null);
    }
}
