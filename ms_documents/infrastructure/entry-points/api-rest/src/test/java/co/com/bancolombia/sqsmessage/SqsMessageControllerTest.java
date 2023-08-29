package co.com.bancolombia.sqsmessage;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.sqs.SqsMessageUplDocUseCase;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_IDMESSAGE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_MICRO_PROFILING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_STATUS_EARRING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_WHICH_MICRO_RECEIVES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_WHICH_OPERATION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SqsMessageControllerTest {

	@InjectMocks
	@Spy
	private SqsMessageController sqsMessageController;

	@Mock
	private SqsMessageUplDocUseCase sqsMessageUploadDocument;

	@Mock
	private SqsMessageUseCase sqsMessageUseCase;

	@Mock
	private LoggerAdapter adapter;

	private SqsMessage sqsMessage;

	private String message;

	private Map<String, String> headers;

	private Acknowledgment acknowledgment;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		acknowledgment = mock(Acknowledgment.class);

		sqsMessage = SqsMessage.builder()
				.sqsRetries(1)
				.sqsMaxRetries(2)
				.statusMessage(SQS_STATUS_EARRING)
				.build();

		headers = new HashMap<>();
		headers.put(SQS_WHICH_MICRO_RECEIVES, SQS_MICRO_PROFILING);
		headers.put(SQS_WHICH_OPERATION, OPER_UPLOAD_DOCUMENT);
		headers.put(SQS_IDMESSAGE, UUID.randomUUID().toString());

		message = "{}";
	}

	@Test
	public void validateAndSendMessageRetriesContainsTest() {
		doReturn(false).when(sqsMessageUploadDocument).readMessage(anyString(), anyMap(), any(SqsMessage.class));

		boolean res = sqsMessageController.validateAndSendMessage(sqsMessage, message, headers);
		assertFalse(res);
	}

	@Test
	public void validateAndSendMessageRetriesNoContainsTest() {
		sqsMessage.setStatusMessage("COMPLETADO");

		boolean res = sqsMessageController.validateAndSendMessage(sqsMessage, message, headers);
		assertTrue(res);
	}

	@Test
	public void validateAndSendMessageNoRetiesTest() {
		sqsMessage.setSqsRetries(3);

		boolean res = sqsMessageController.validateAndSendMessage(sqsMessage, message, headers);
		assertTrue(res);
	}

	@Test
	public void exceptionHandlerTest() {
		Exception e = new Exception();
		sqsMessageController.exceptionHandler(e);
		verify(sqsMessageController, times(1)).exceptionHandler(any(Exception.class));
	}

	@Test
	public void getMessageFromSqsNoPresentTest() throws ExecutionException, InterruptedException {
		doReturn(Optional.ofNullable(null)).when(sqsMessageUseCase).findById(any(UUID.class));

		sqsMessageController.getMessageFromSqs(message, acknowledgment, headers);
		verify(sqsMessageController, never()).validateAndSendMessage(any(SqsMessage.class), anyString(), anyMap());
	}

	@Test(expected = NullPointerException.class)
	public void getMessageFromSqsPresentTest() throws ExecutionException, InterruptedException {
		doReturn(Optional.ofNullable(sqsMessage)).when(sqsMessageUseCase).findById(any(UUID.class));
		doReturn(true).when(sqsMessageController).validateAndSendMessage(any(SqsMessage.class), anyString(), anyMap());

		sqsMessageController.getMessageFromSqs(message, acknowledgment, headers);
	}

	@Test
	public void getMessageFromSqsExceptionTest() throws ExecutionException, InterruptedException {
		doReturn(Optional.ofNullable(sqsMessage)).when(sqsMessageUseCase).findById(any(UUID.class));
		doReturn(true).when(sqsMessageController).validateAndSendMessage(any(SqsMessage.class), anyString(), anyMap());
		doThrow(CustomException.class).when(acknowledgment).acknowledge();

		sqsMessageController.getMessageFromSqs(message, acknowledgment, headers);
		verify(sqsMessageController, times(1)).validateAndSendMessage(any(SqsMessage.class), anyString(), anyMap());
	}

	@Test
	public void getMessageFromSqsNoValidMicroAndOperationTest() throws ExecutionException, InterruptedException {
		headers.put(SQS_WHICH_MICRO_RECEIVES, "OTHER_MICRO");
		headers.put(SQS_WHICH_OPERATION, "other-operation");

		sqsMessageController.getMessageFromSqs(message, acknowledgment, headers);
		verify(sqsMessageUseCase, never()).findById(any(UUID.class));
	}

	@Test
	public void getMessageFromSqsNoValidMicroTest() throws ExecutionException, InterruptedException {
		headers.put(SQS_WHICH_MICRO_RECEIVES, "OTHER_MICRO");

		sqsMessageController.getMessageFromSqs(message, acknowledgment, headers);
		verify(sqsMessageUseCase, never()).findById(any(UUID.class));
	}

	@Test
	public void getMessageFromSqsNoValidOperationTest() throws ExecutionException, InterruptedException {
		headers.put(SQS_WHICH_OPERATION, "other-operation");

		sqsMessageController.getMessageFromSqs(message, acknowledgment, headers);
		verify(sqsMessageUseCase, never()).findById(any(UUID.class));
	}
}
