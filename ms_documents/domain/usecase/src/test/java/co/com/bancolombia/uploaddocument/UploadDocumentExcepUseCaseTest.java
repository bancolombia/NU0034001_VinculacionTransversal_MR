package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.model.documentretries.DocumentRetries;
import co.com.bancolombia.model.sqs.SqsMessaPararmSave;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxError;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentError;
import co.com.bancolombia.model.uploaddocument.UploadDocumentErrorResponse;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.sqs.SqsMessageUplDocUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_PROCESS_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_UPLOAD_DOCUMENT_RETRY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class UploadDocumentExcepUseCaseTest {

	@InjectMocks
	@Spy
	private UploadDocumentExcepUseCaseImpl uploadDocumentExcepUseCase;

	@Mock
	private VinculationUpdateUseCase vinculationUpdateUseCase;;

	@Mock
	private SqsMessageUplDocUseCase sqsMessageUplDocUseCase;

	@Mock
	private UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase;

	@Mock
	private DocumentRetriesUseCase documentRetriesUseCase;

	private UploadDocumentApiResponse uploadDocumentApiResponse;

	private SqsMessageParamAllObject sqsMessageParamAllObject;

	private UploadDocumentWithLog uploadDocumentWithLog;

	private InfoReuseCommon infoReuseCommon;

	private UploadDocumentTotal uploadDocumentTotal;

	private UploadDocumentErrorResponse uploadDocumentErrorResponse;

	private UploadDocumentError uploadDocumentError;

	private ProcessDocumentKofaxTotal processDocumentKofaxTotal;

	private SqsMessaPararmSave sqsMessaPararmSave;

	private Acquisition acquisition;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		acquisition = Acquisition.builder()
				.id(UUID.randomUUID())
				.uploadDocumentRetries(0)
				.uploadRutRetries(0)
				.build();

		uploadDocumentError = UploadDocumentError.builder()
				.code("code")
				.detail("detail")
				.build();

		List<UploadDocumentError> uploadDocumentErrors = Arrays.asList(uploadDocumentError);

		uploadDocumentErrorResponse = UploadDocumentErrorResponse.builder()
				.errors(uploadDocumentErrors)
				.title("title")
				.build();

		uploadDocumentTotal = UploadDocumentTotal.builder()
				.uploadDocumentErrorResponse(uploadDocumentErrorResponse)
				.build();

		infoReuseCommon = InfoReuseCommon.builder()
				.dateRequestReuse(new CoreFunctionDate().getDatetime())
				.dateResponseReuse(new CoreFunctionDate().getDatetime())
				.requestReuse("{\"data\": \"data\"}")
				.responseReuse("{\"data\": \"data\"}")
				.build();

		processDocumentKofaxTotal = ProcessDocumentKofaxTotal.builder()
				.build();

		uploadDocumentWithLog = UploadDocumentWithLog.builder()
				.infoReuseCommon(infoReuseCommon)
				.uploadDocumentTotal(uploadDocumentTotal)
				.processDocumentKofaxTotal(processDocumentKofaxTotal)
				.build();

		uploadDocumentApiResponse = UploadDocumentApiResponse.builder()
				.data(uploadDocumentWithLog)
				.build();

		sqsMessageParamAllObject = SqsMessageParamAllObject.builder()
				.sqsMessage(SqsMessage.builder().build())
				.build();

		sqsMessaPararmSave = SqsMessaPararmSave.builder()
				.acquisition(sqsMessageParamAllObject.getAcquisition())
				.obj(sqsMessageParamAllObject.getSqsMessObjUploadDoc())
				.uploadDocumentWithLog(uploadDocumentWithLog)
				.sqsMessage(sqsMessageParamAllObject.getSqsMessage())
				.saveAsync(true)
				.build();
	}

	@Test
	public void validateBackEndExceptionTest() {
		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateBackEndException(uploadDocumentApiResponse, sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}

	@Test
	public void validateBusinessException049CcTest() {
		DocumentRetries documentRetries = DocumentRetries.builder().build();
		ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
				.code(ERROR_CODE_UPLOAD_DOCUMENT_RETRY).detail(TYPE_CC).build();

		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doReturn(documentRetries).when(documentRetriesUseCase).save(any(Acquisition.class));
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateBusinessException(
				processDocumentKofaxError, acquisition, sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}

	@Test
	public void validateBusinessException049RutTest() {
		DocumentRetries documentRetries = DocumentRetries.builder().build();
		ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
				.code(ERROR_CODE_UPLOAD_DOCUMENT_RETRY).detail(TYPE_RUT).build();

		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doReturn(documentRetries).when(documentRetriesUseCase).save(any(Acquisition.class));
		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateBusinessException(
				processDocumentKofaxError, acquisition, sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}

	@Test
	public void validateBusinessException048Test() {
		ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
				.code(ERROR_CODE_PROCESS_DOCUMENT).detail("").build();

		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateBusinessException(
				processDocumentKofaxError, acquisition, sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}

	@Test
	public void validateBusinessException050Test() {
		ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
				.code(ERROR_CODE_AVAILABLE_FILL_OUT_MANUAL).detail("").build();

		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(uploadDocumentProcessedDocumentsUseCase).saveProcessedDocuments(
				any(UploadDocumentParameter.class), any(Date.class), any(SqsMessageParamAllObject.class), any(Map.class));
		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateBusinessException(
				processDocumentKofaxError, acquisition, sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}


	@Test
	public void validateBusinessException053Test() {
		ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
				.code(ERROR_CODE_FILL_OUT_DATE_EXPEDITION_MANUAL).detail("").build();

		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
		doNothing().when(uploadDocumentProcessedDocumentsUseCase).saveProcessedDocuments(
				any(UploadDocumentParameter.class), any(Date.class), any(SqsMessageParamAllObject.class), any(Map.class));
		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateBusinessException(
				processDocumentKofaxError, acquisition, sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}

	@Test
	public void validateBusinessExceptionOtherTest() {
		ProcessDocumentKofaxError processDocumentKofaxError = ProcessDocumentKofaxError.builder()
				.code("BVNT000").detail("").build();

		uploadDocumentExcepUseCase.validateBusinessException(processDocumentKofaxError, acquisition, null);
		verify(sqsMessageUplDocUseCase, never()).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}

	@Test(expected = ValidationException.class)
	public void validateExceptionNullSqsParamTest() {
		uploadDocumentExcepUseCase.validateException("", "", "", null);
	}

	@Test(expected = ValidationException.class)
	public void validateExceptionNullSqsMessageTest() {
		sqsMessageParamAllObject.setSqsMessage(null);
		uploadDocumentExcepUseCase.validateException("", "", "", sqsMessageParamAllObject);
	}

	@Test
	public void validateExceptionNullKofaxTest() {
		uploadDocumentApiResponse.getData().setProcessDocumentKofaxTotal(null);
		sqsMessageParamAllObject.setUploadDocumentParameter(UploadDocumentParameter.builder().build());
		sqsMessageParamAllObject.setUploadDocumentApiResponse(uploadDocumentApiResponse);

		doNothing().when(sqsMessageUplDocUseCase).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));

		uploadDocumentExcepUseCase.validateException("", "", "", sqsMessageParamAllObject);
		verify(sqsMessageUplDocUseCase, times(1)).saveAndUpdateSqsMessage(any(SqsMessaPararmSave.class));
	}
}
