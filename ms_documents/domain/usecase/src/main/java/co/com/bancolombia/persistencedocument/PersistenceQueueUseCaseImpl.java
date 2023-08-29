package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.gateways.ServicePersistenceRestRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CUSTOMER_DOC_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_SEND_MESSAGE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SQS_SEND_MESSAGE_OK;
import static co.com.bancolombia.util.constants.Constants.ERROR_DETAIL_FETCHING_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.NO_APPLY;

@RequiredArgsConstructor
public class PersistenceQueueUseCaseImpl implements PersistenceQueueUseCase {

    @Value("${aws.sqsPersistence.queueUrl}")
    private String sqsUrl;

    private final SqsAsyncClient sqsAsyncClient;

    private final CoreFunctionDate coreFunctionDate;

    private final PersistenceDocumentValidationUseCase pDocUC;

    private final ServicePersistenceRestRepository microServiceRestRepository;

    private static LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, SERVICE_PERSISTENCE,
            OPER_CUSTOMER_DOC_PERSISTENCE);

    @Override
    public void sendMessagesToQueue(TdcDocument tdcDocument) {
        Gson gson = new Gson();
        String tdcDocumentBody = gson.toJson(tdcDocument, TdcDocument.class);
        CompletableFuture<SendMessageResponse> result =
                sqsAsyncClient.sendMessage(SendMessageRequest.builder()
                        .queueUrl(sqsUrl)
                        .delaySeconds(Numbers.THREE.getIntNumber())
                        .messageBody(tdcDocumentBody)
                        .build());
        try {
            if (result.get() != null) {
                adapter.info(SQS_SEND_MESSAGE_OK);
            }
        } catch (ExecutionException | InterruptedException e) {
            adapter.error(SQS_SEND_MESSAGE_ERROR);
            adapter.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void sendMessagesToRetriesQueue(Map<PersistenceDocument, TdcDocument> map) {
        map.forEach((key, value)
                -> {
            if (key.getData().stream().noneMatch(x -> x.getErrorDescription().equals(NO_APPLY)) &&
                    key.getData().stream()
                            .noneMatch(x -> x.getErrorDescription().equals(ERROR_DETAIL_FETCHING_DOCUMENT))) {
                sendMessagesToQueue(value);
            }
        });
    }

    public void retrieveMessages() {
        List<PersistenceDocument> documentWithLogList = new ArrayList<>();
        Map<PersistenceDocument, TdcDocument> map = new HashMap<>();
        try {
            boolean flag = true;
            while (flag) {
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(sqsUrl).build();
                List<Message> messages = sqsAsyncClient.receiveMessage(receiveMessageRequest).get().messages();
                Gson gson = new Gson();
                for (Message message : messages) {
                    TdcDocument tdcDoc = gson.fromJson(message.body(), TdcDocument.class);
                    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(sqsUrl)
                            .receiptHandle(message.receiptHandle()).build();
                    sqsAsyncClient.deleteMessage(deleteMessageRequest);
                    PersistenceDocument pDocumentLog = pDocUC.persistenceDocumentTDC(tdcDoc, "RETRIES", Boolean.TRUE);
                    pDocumentLog.setInitOperation(coreFunctionDate.getDatetime());
                    pDocumentLog.setDocumentType(tdcDoc.getDocumentType());
                    documentWithLogList.add(pDocumentLog);
                    map.put(pDocumentLog, tdcDoc);
                }
                flag = !messages.isEmpty();
            }
            sendMessagesToRetriesQueue(map);
        } catch (SqsException | ExecutionException | InterruptedException e) {
            adapter.error("Error receiving messages", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        sendRetries(documentWithLogList);
    }

    public void sendRetries(List<PersistenceDocument> documentWithLogList) {
        if (!documentWithLogList.isEmpty()) {
            microServiceRestRepository.sendPersistenceDocumentRetriesInfo(documentWithLogList);
        }
    }
}
