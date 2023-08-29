package co.com.bancolombia.sqs;

import co.com.bancolombia.asyncdigitalization.AsyncDigitalizationUseCase;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessaPararmSave;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SqsMessageSaveUseCaseTest {
	
	@InjectMocks
	@Spy
	private SqsMessageSaveUseCaseImpl sqsMessageSaveUseCase;

	@Mock
	private SqsMessageUseCase sqsMessageUseCase;

	@Mock
	private AsyncDigitalizationUseCase asyncDigitalizationUseCase;

	@Mock
	private SqsMessObjUploadDocUtil sqsMessObjUploadDocUtil;

	private SqsMessaPararmSave sqsMessaPararmSave;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		sqsMessaPararmSave = SqsMessaPararmSave.builder()
				.acquisition(Acquisition.builder().build())
				.uploadDocumentWithLog(UploadDocumentWithLog.builder().build())
				.obj(SqsMessObjUploadDoc.builder().build())
				.sqsMessage(SqsMessage.builder().build())
				.build();
	}
	
	@Test
	public void saveAndUpdateSqsMessageAsyncPresentTest() {
		AsyncDigitalization async = AsyncDigitalization.builder().build();
		doReturn(async).when(sqsMessObjUploadDocUtil).constructAsyncDigitalization(
				any(Acquisition.class), any(SqsMessObjUploadDoc.class), any(UploadDocumentWithLog.class),
				any(SqsMessage.class));
		doReturn(Optional.of(async)).when(asyncDigitalizationUseCase).findBySqsMessage(any(SqsMessage.class));
		doReturn(async).when(asyncDigitalizationUseCase).save(any(AsyncDigitalization.class));
		doReturn(SqsMessage.builder().build()).when(sqsMessageUseCase).saveSqsMessage(any(SqsMessage.class));

		sqsMessaPararmSave.setSaveAsync(true);

		sqsMessageSaveUseCase.saveAndUpdateSqsMessage(sqsMessaPararmSave);
		verify(asyncDigitalizationUseCase, never()).save(any(AsyncDigitalization.class));
	}

	@Test
	public void saveAndUpdateSqsMessageAsyncEmptyTest() {
		AsyncDigitalization async = AsyncDigitalization.builder().build();
		doReturn(async).when(sqsMessObjUploadDocUtil).constructAsyncDigitalization(
				any(Acquisition.class), any(SqsMessObjUploadDoc.class), any(UploadDocumentWithLog.class),
				any(SqsMessage.class));
		doReturn(Optional.empty()).when(asyncDigitalizationUseCase).findBySqsMessage(any(SqsMessage.class));
		doReturn(async).when(asyncDigitalizationUseCase).save(any(AsyncDigitalization.class));
		doReturn(SqsMessage.builder().build()).when(sqsMessageUseCase).saveSqsMessage(any(SqsMessage.class));

		sqsMessaPararmSave.setSaveAsync(true);

		sqsMessageSaveUseCase.saveAndUpdateSqsMessage(sqsMessaPararmSave);
		verify(asyncDigitalizationUseCase, times(1)).save(any(AsyncDigitalization.class));
	}

	@Test
	public void saveAndUpdateSqsMessageSyncTest() {
		doReturn(SqsMessage.builder().build()).when(sqsMessageUseCase).saveSqsMessage(any(SqsMessage.class));

		sqsMessaPararmSave.setSaveAsync(false);

		sqsMessageSaveUseCase.saveAndUpdateSqsMessage(sqsMessaPararmSave);
		verify(asyncDigitalizationUseCase, never()).save(any(AsyncDigitalization.class));
	}
	
	@Test
	public void constructUploadDocumentParameterTest() {
		UploadDocumentParameter upload = UploadDocumentParameter.builder().build();
		doReturn(upload).when(sqsMessObjUploadDocUtil).constructUploadDocumentParameter(
				any(Acquisition.class), any(SqsMessObjUploadDoc.class), any(List.class));

		Acquisition acquisition = Acquisition.builder().build();
		SqsMessObjUploadDoc obj = SqsMessObjUploadDoc.builder().build();
		List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());

		UploadDocumentParameter uploadDoc = sqsMessageSaveUseCase.constructUploadDocumentParameter(
				acquisition, obj, processDocumentFiles);

		assertEquals(upload, uploadDoc);
	}
}
