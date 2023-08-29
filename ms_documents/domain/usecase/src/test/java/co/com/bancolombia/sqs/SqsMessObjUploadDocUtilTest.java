package co.com.bancolombia.sqs;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.sqs.SqsMessAcqObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessDataObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxTotal;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SqsMessObjUploadDocUtilTest {

	@InjectMocks
	@Spy
	private SqsMessObjUploadDocUtilImpl sqsMessObjUploadDocUtilImpl;

	@Mock
	private SqsMessageUplDocUtilUseCase sqsMessageUplDocUtilUseCase;
	
	private SqsMessObjUploadDoc obj;

	private Acquisition acquisition;

	private SqsMessDataObjUploadDoc sqsMessDataObjUploadDoc;

	private SqsMessAcqObjUploadDoc sqsMessAcqObjUploadDoc;

	private UploadDocumentWithLog uploadDocumentWithLog;

	private SqsMessage sqsMessage;

	private AsyncDigitalization asyncDigitalization;

	private InfoReuseCommon infoReuseCommon;

	private UploadDocumentTotal uploadDocumentTotal;

	private UploadDocumentResponse uploadDocumentResponse;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		UUID idAcq = UUID.randomUUID();
		acquisition = Acquisition.builder().id(idAcq).build();
		sqsMessAcqObjUploadDoc = SqsMessAcqObjUploadDoc.builder().id(idAcq.toString()).build();
		sqsMessDataObjUploadDoc = SqsMessDataObjUploadDoc.builder().acquisition(sqsMessAcqObjUploadDoc).build();
		obj = SqsMessObjUploadDoc.builder().data(sqsMessDataObjUploadDoc).build();
		infoReuseCommon = InfoReuseCommon.builder().requestReuse("").responseReuse("").build();
		sqsMessage = SqsMessage.builder().build();
		asyncDigitalization = AsyncDigitalization.builder().build();
		uploadDocumentTotal = UploadDocumentTotal.builder().build();
		uploadDocumentResponse = UploadDocumentResponse.builder().build();

		uploadDocumentWithLog = UploadDocumentWithLog.builder()
				.infoReuseCommon(infoReuseCommon)
				.processDocumentKofaxTotal(ProcessDocumentKofaxTotal.builder().build())
				.build();
	}
	
	@Test
	public void constructUploadDocumentParameterTest() {
		List<ProcessDocumentFiles> processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder().build());

		UploadDocumentParameter docParam = sqsMessObjUploadDocUtilImpl.constructUploadDocumentParameter(
				acquisition, obj, processDocumentFiles);

		UploadDocumentParameter docParamExpected = UploadDocumentParameter.builder()
				.acquisition(acquisition)
				.processDocumentFiles(processDocumentFiles)
				.build();
		
		assertEquals(docParamExpected, docParam);
	}
	
	@Test
	public void constructAsyncDigitalizationNullUplDocTotalTest() {
		uploadDocumentWithLog.setUploadDocumentTotal(null);

		doReturn("").when(sqsMessageUplDocUtilUseCase).forEachObjectJson(anyString(), any(List.class));

		AsyncDigitalization async = sqsMessObjUploadDocUtilImpl.constructAsyncDigitalization(
				acquisition, obj, uploadDocumentWithLog, sqsMessage);
		assertNotNull(async);
	}

	
	@Test
	public void constructAsyncDigitalizationNullUplDocResponseTest() {
		uploadDocumentTotal.setUploadDocumentResponse(null);
		uploadDocumentWithLog.setUploadDocumentTotal(uploadDocumentTotal);

		doReturn("").when(sqsMessageUplDocUtilUseCase).forEachObjectJson(anyString(), any(List.class));

		AsyncDigitalization async = sqsMessObjUploadDocUtilImpl.constructAsyncDigitalization(
				acquisition, obj, uploadDocumentWithLog, sqsMessage);
		assertNotNull(async);
	}

	@Test
	public void constructAsyncDigitalizationNotNullUplDocResponseTest() {
		uploadDocumentTotal.setUploadDocumentResponse(uploadDocumentResponse);
		uploadDocumentWithLog.setUploadDocumentTotal(uploadDocumentTotal);

		doReturn("").when(sqsMessageUplDocUtilUseCase).forEachObjectJson(anyString(), any(List.class));
		doReturn("").when(sqsMessageUplDocUtilUseCase).buildUploadDocumentResponse(
				any(SqsMessObjUploadDoc.class), any(UploadDocumentResponse.class));

		AsyncDigitalization async = sqsMessObjUploadDocUtilImpl.constructAsyncDigitalization(
				acquisition, obj, uploadDocumentWithLog, sqsMessage);
		assertNotNull(async);
	}
}
