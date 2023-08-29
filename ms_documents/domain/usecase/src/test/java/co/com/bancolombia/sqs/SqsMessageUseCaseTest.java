package co.com.bancolombia.sqs;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import co.com.bancolombia.model.sqs.gateways.SqsMessageRepository;
import co.com.bancolombia.model.uploaddocument.ProcessDocumentFiles;
import co.com.bancolombia.model.uploaddocument.UploadDocumentFile;
import co.com.bancolombia.model.uploaddocument.UploadDocumentParameter;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SqsMessageUseCaseTest {

	@InjectMocks
	@Spy
	private SqsMessageUseCaseImpl sqsMessageUseCase;

	@Mock
	private SqsMessageRepository sqsMessageRepository;

	@Mock
	private CoreFunctionDate coreFunctionDate;

	@Mock
	private SqsMessageParameterUseCase sqsMessageParameterUseCase;

	@Mock
	private SqsAsyncClient sqsAsyncClient;

	@Mock
	private SqsMessageJsonUseCase sqsMessageJsonUseCase;

	private SqsMessage sqsMessage;

	private List<ProcessDocumentFiles> processDocumentFiles;

	private Acquisition acquisition;

	private UploadDocumentParameter upload;

	private List<UploadDocumentFile> listGet;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		acquisition = Acquisition.builder()
				.id(UUID.randomUUID())
				.documentNumber("10000000")
				.documentType(DocumentType.builder()
						.code("TIPDOC_FS001")
						.name("CEDULA")
						.build())
				.typeAcquisition(TypeAcquisition.builder()
						.code("TVN001")
						.name("001")
						.build())
				.uploadDocumentRetries(0)
				.uploadRutRetries(0)
				.build();

		sqsMessage = SqsMessage.builder()
				.id(UUID.randomUUID())
				.operation(OPER_UPLOAD_DOCUMENT)
				.typeAcquisition("")
				.acquisition(acquisition)
				.documentType("")
				.documentNumber("")
				.request("")
				.response("")
				.message("")
				.statusMessage("")
				.helpField("")
				.build();

		processDocumentFiles = Arrays.asList(ProcessDocumentFiles.builder()
				.base64("Base64")
				.extension("application/pdf")
				.fileName("001_TIPDOC_FS001_1000000_1.pdf")
				.build());

		listGet = Arrays.asList(UploadDocumentFile.builder()
				.fileName("001_TIPDOC_FS001_1000000_1.pdf")
				.build());

		upload = UploadDocumentParameter.builder()
				.processDocumentFiles(processDocumentFiles)
				.acquisition(acquisition)
				.meta(SqsMetaUploadDocument.builder().build())
				.listGet(listGet)
				.build();
	}

	@Test
	public void saveSqsMessageTest() {
		doReturn(sqsMessage).when(sqsMessageRepository).save(any(SqsMessage.class));
		SqsMessage sqs = sqsMessageUseCase.saveSqsMessage(sqsMessage);
		assertNotNull(sqs);
	}

	@Test
	public void sendSqsMessageProcessDocumentCcTest() {
		CompletableFuture<SendMessageResponse> completable = CompletableFuture.completedFuture(
				SendMessageResponse.builder().build());

		doReturn(3).when(sqsMessageParameterUseCase).returnParameterMaxRetriesReadMessage();
		doReturn(sqsMessage).when(sqsMessageRepository).save(any(SqsMessage.class));
		doReturn(completable).when(sqsAsyncClient).sendMessage(any(SendMessageRequest.class));

		upload.setDocumentSubtype(CEDULA_SUBTYPE);
		upload.setProcessDocumentFiles(processDocumentFiles);
		upload.setListGet(listGet);

		sqsMessageUseCase.sendSqsMessageProcessDocument(upload, "");
		verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
	}

	@Test
	public void sendSqsMessageProcessDocumentRutTest() {
		CompletableFuture<SendMessageResponse> completable = CompletableFuture.completedFuture(
				SendMessageResponse.builder().build());

		doReturn(3).when(sqsMessageParameterUseCase).returnParameterMaxRetriesReadMessage();
		doReturn(sqsMessage).when(sqsMessageRepository).save(any(SqsMessage.class));
		doReturn(completable).when(sqsAsyncClient).sendMessage(any(SendMessageRequest.class));

		upload.setDocumentSubtype(RUT_SUBTYPE);
		upload.setProcessDocumentFiles(processDocumentFiles);
		upload.setListGet(Collections.emptyList());

		sqsMessageUseCase.sendSqsMessageProcessDocument(upload, "");
		verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
	}

	@Test
	public void sendSqsMessageProcessDocumentEmptyListGetTest() {
		CompletableFuture<SendMessageResponse> completable = CompletableFuture.completedFuture(null);

		doReturn(3).when(sqsMessageParameterUseCase).returnParameterMaxRetriesReadMessage();
		doReturn(sqsMessage).when(sqsMessageRepository).save(any(SqsMessage.class));
		doReturn(completable).when(sqsAsyncClient).sendMessage(any(SendMessageRequest.class));

		upload.setDocumentSubtype(CEDULA_SUBTYPE);
		upload.setProcessDocumentFiles(Collections.emptyList());
		upload.setListGet(listGet);

		sqsMessageUseCase.sendSqsMessageProcessDocument(upload, "");
		verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
	}

	@Test
	public void sendSqsMessageProcessDocumentNullCompletableTest() {
		CompletableFuture<SendMessageResponse> completable = CompletableFuture.completedFuture(null);

		doReturn(3).when(sqsMessageParameterUseCase).returnParameterMaxRetriesReadMessage();
		doReturn(sqsMessage).when(sqsMessageRepository).save(any(SqsMessage.class));
		doReturn(completable).when(sqsAsyncClient).sendMessage(any(SendMessageRequest.class));

		upload.setDocumentSubtype(CEDULA_SUBTYPE);
		upload.setProcessDocumentFiles(processDocumentFiles);
		upload.setListGet(listGet);

		sqsMessageUseCase.sendSqsMessageProcessDocument(upload, "");
		verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
	}

	@Test
	public void findMessageTest() {
		doReturn(sqsMessage).when(sqsMessageRepository)
				.findTopByOperationAndAcquisitionOrderByCreatedDateDesc(anyString(), any(Acquisition.class));

		Optional<SqsMessage> message = sqsMessageUseCase.findMessage(OPER_UPLOAD_DOCUMENT, acquisition);
		assertTrue(message.isPresent());
	}

	@Test
	public void findByIdTest() {
		doReturn(sqsMessage).when(sqsMessageRepository).findById(any(UUID.class));

		Optional<SqsMessage> message = sqsMessageUseCase.findById(UUID.randomUUID());
		assertTrue(message.isPresent());
	}

	@Test
	public void findMessageListTest() {
		doReturn(Arrays.asList(sqsMessage)).when(sqsMessageRepository)
				.findByOperationAndAcquisitionOrderByCreatedDateDesc(anyString(), any(Acquisition.class));

		List<SqsMessage> messages = sqsMessageUseCase.findMessageList(OPER_UPLOAD_DOCUMENT, acquisition);
		assertEquals(1, messages.size());
	}
}
