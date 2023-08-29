package co.com.bancolombia.sqsmessage;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.sqs.SqsMessage;
import co.com.bancolombia.sqs.SqsMessageUplDocUseCase;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
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

@Component
public class SqsMessageController {

	@Autowired
	private SqsMessageUplDocUseCase sqsMessageUploadDocument;

	@Autowired
	private SqsMessageUseCase sqsMessageUseCase;

	@Resource(name = "adapterSqsMessageController")
	private LoggerAdapter adapter;

	@MessageExceptionHandler(Exception.class)
	public void exceptionHandler(Exception e) {
		adapter.error("MessageConsumer error", e);
	}

	@SqsListener(value = "${aws.sqs.queueName}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void getMessageFromSqs(
			@Valid String message, Acknowledgment acknowledgment, @Headers Map<String, String> headers)
			throws ExecutionException, InterruptedException {

		boolean result = false;
		String idMessage = headers.get(SQS_IDMESSAGE);

		adapter.info("---------------------------");
		adapter.info("SQS Leyendo el mensaje con ID: " + idMessage);

		if (SQS_MICRO_PROFILING.equals(headers.get(SQS_WHICH_MICRO_RECEIVES))
				&& OPER_UPLOAD_DOCUMENT.equals(headers.get(SQS_WHICH_OPERATION))) {

			Optional<SqsMessage> sqsMessage = sqsMessageUseCase.findById(
					UUID.fromString(headers.get(SQS_IDMESSAGE)));

			if (sqsMessage.isPresent()) {
				result = validateAndSendMessage(sqsMessage.get(), message, headers);
			}
		}

		if (result) {
			try {
				acknowledgment.acknowledge().get();
				adapter.info("Mensaje ha sido eliminado");
			} catch (CustomException e) {
				adapter.error("Errror eliminando el mensaje");
			}
		}

		adapter.info("Finalizando la lectura del mensaje con ID: " + idMessage + " Valor variable Result: " + result);
	}

	public boolean validateAndSendMessage(SqsMessage sqsMessage, String message, Map<String, String> headers) {
		boolean result;
		if (sqsMessage.getSqsRetries() <= sqsMessage.getSqsMaxRetries()) {
			if (SQS_STATUS_EARRING.contains(sqsMessage.getStatusMessage())) {
				result = sqsMessageUploadDocument.readMessage(message, headers, sqsMessage);
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
}
