package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentError;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;
import co.com.bancolombia.model.persistencedocument.gateways.ServicePersistenceRestRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;
import software.amazon.awssdk.utils.CompletableFutureUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static co.com.bancolombia.util.constants.Constants.ERROR_DETAIL_FETCHING_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.ERROR_DETAIL_TIMEOUT_PERSISTING_DOCUMENT;
import static co.com.bancolombia.util.constants.Constants.NO_APPLY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class PersistenceQueueUseCaseTest {

    @InjectMocks
    @Spy
    PersistenceQueueUseCaseImpl persistenceQueueUseCase;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private PersistenceDocumentValidationUseCase pDocUC;

    @Mock
    private ServicePersistenceRestRepository microServiceRestRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = RuntimeException.class)
    public void retrieveMessagesTest() {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();
        documentsFileList.add(0, tdcDocumentsFileCC);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();
        String jsonInfo = new Gson().toJson(tdcDocument);
        List<Message> messages = Collections.singletonList(Message.builder().body(jsonInfo)
                .messageId(UUID.randomUUID().toString()).build());
        Supplier<ReceiveMessageResponse> supplier = () -> ReceiveMessageResponse.builder().messages(messages).build();
        CompletableFuture<ReceiveMessageResponse> receiveMessageResponse = CompletableFuture.supplyAsync(supplier)
                .toCompletableFuture();
        doReturn(receiveMessageResponse).when(sqsAsyncClient).receiveMessage(any(ReceiveMessageRequest.class));

        this.persistenceQueueUseCase.retrieveMessages();
        verify(this.persistenceQueueUseCase, times(1)).retrieveMessages();
    }

    /*
    @Test (expected = OutOfMemoryError.class)
    public void retrieveMessagesTwoTest() throws ExecutionException, InterruptedException, SqsException {
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");
        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();
        documentsFileList.add(0, tdcDocumentsFileCC);
        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();
        String jsonInfo = new Gson().toJson(tdcDocument);
        List<Message> messages = Collections.singletonList(Message.builder().body(jsonInfo)
                .messageId(UUID.randomUUID().toString()).build());

        doReturn(CompletableFuture.completedFuture(ReceiveMessageResponse.builder().messages(messages).build())
                        .toCompletableFuture()).when(sqsAsyncClient).receiveMessage(any(ReceiveMessageRequest.class));
        doReturn(null).when(sqsAsyncClient).deleteMessage(any(DeleteMessageRequest.class));
        doReturn(PersistenceDocument.builder().build()).when(pDocUC).persistenceDocumentTDC(any(TdcDocument.class),
                anyString(), anyBoolean());
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doNothing().when(persistenceQueueUseCase).sendMessagesToQueue(any(TdcDocument.class));
        doNothing().when(microServiceRestRepository).sendPersistenceDocumentRetriesInfo(anyList());

        this.persistenceQueueUseCase.retrieveMessages();
        verify(this.persistenceQueueUseCase, times(1)).retrieveMessages();
    }
*/

    @Test
    public void sendMessagesToRetriesQueueTest() {
        Map<PersistenceDocument, TdcDocument> map = new HashMap<>();
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentError(PersistenceDocumentError.builder().description("").build())
                .build();
        PersistenceDocumentList persistenceDocument = PersistenceDocumentList.builder().
                errorDescription(ERROR_DETAIL_TIMEOUT_PERSISTING_DOCUMENT).build();
        persistenceDocumentWithLog.setData(Collections.singletonList(persistenceDocument));

        map.put(persistenceDocumentWithLog, tdcDocument);

        doNothing().when(this.persistenceQueueUseCase).sendMessagesToQueue(any(TdcDocument.class));
        this.persistenceQueueUseCase.sendMessagesToRetriesQueue(map);
        verify(this.persistenceQueueUseCase, times(1)).sendMessagesToRetriesQueue(map);
    }

    @Test
    public void sendMessagesToRetrieQueueSecondTest() {
        Map<PersistenceDocument, TdcDocument> map = new HashMap<>();
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentError(PersistenceDocumentError.builder().description("").build())
                .build();
        PersistenceDocumentList persistenceDocument = PersistenceDocumentList.builder().
                errorDescription(ERROR_DETAIL_FETCHING_DOCUMENT).build();
        persistenceDocumentWithLog.setData(Collections.singletonList(persistenceDocument));

        map.put(persistenceDocumentWithLog, tdcDocument);

        this.persistenceQueueUseCase.sendMessagesToRetriesQueue(map);
        verify(this.persistenceQueueUseCase, times(1)).sendMessagesToRetriesQueue(map);
    }

    @Test
    public void sendMessagesToRetrieQueueThirdTest() {
        Map<PersistenceDocument, TdcDocument> map = new HashMap<>();
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        PersistenceDocument persistenceDocumentWithLog = PersistenceDocument.builder()
                .persistenceDocumentError(PersistenceDocumentError.builder().description("").build())
                .build();
        PersistenceDocumentList persistenceDocument = PersistenceDocumentList.builder().
                errorDescription(NO_APPLY).build();
        persistenceDocumentWithLog.setData(Collections.singletonList(persistenceDocument));

        map.put(persistenceDocumentWithLog, tdcDocument);

        this.persistenceQueueUseCase.sendMessagesToRetriesQueue(map);
        verify(this.persistenceQueueUseCase, times(1)).sendMessagesToRetriesQueue(map);
    }

    @Test
    public void sendMessageTest() {
        CompletableFuture<SendMessageResponse> sendMessage = CompletableFuture.completedFuture(SendMessageResponse.builder().build());
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        when(this.sqsAsyncClient.sendMessage(ArgumentMatchers.any(SendMessageRequest.class))).thenReturn(sendMessage);
        this.persistenceQueueUseCase.sendMessagesToQueue(tdcDocument);
        verify(persistenceQueueUseCase,times(1)).sendMessagesToQueue(tdcDocument);
    }

    @Test
    public void sendMessageNullMessageTest() {
        CompletableFuture<SendMessageResponse> sendMessage = CompletableFuture.completedFuture(null);
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        when(this.sqsAsyncClient.sendMessage(ArgumentMatchers.any(SendMessageRequest.class))).thenReturn(sendMessage);
        this.persistenceQueueUseCase.sendMessagesToQueue(tdcDocument);
        verify(persistenceQueueUseCase,times(1)).sendMessagesToQueue(tdcDocument);
    }


    @Test
    public void sendMessageInterruptedExceptionTest() throws ExecutionException, InterruptedException {
        CompletableFuture<SendMessageResponse> sendMessage = new CompletableFuture<>();
        List<TdcDocumentsFile> documentsFileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(0, "NameOne.pdf");

        TdcDocumentsFile tdcDocumentsFileCC = TdcDocumentsFile.builder()
                .documentalSubTypeCode("002").documentalTypeCode("01").fileNames(fileNameList).build();

        documentsFileList.add(0, tdcDocumentsFileCC);

        TdcDocument tdcDocument = TdcDocument.builder().documentNumber("12345678")
                .acquisitionId(UUID.randomUUID().toString()).documentsFileList(documentsFileList).build();

        sendMessage.completeExceptionally(new InterruptedException());

        Mockito.when(this.sqsAsyncClient.sendMessage(ArgumentMatchers.any(SendMessageRequest.class))).thenReturn(sendMessage);
        this.persistenceQueueUseCase.sendMessagesToQueue(tdcDocument);
        verify(persistenceQueueUseCase,times(1)).sendMessagesToQueue(tdcDocument);
    }

    @Test
    public void sendRetriesTest(){
        PersistenceDocumentList persistenceDocumentList = PersistenceDocumentList.builder().idDocument("asd").build();
        List<PersistenceDocumentList> documentsList = new ArrayList<>();
        documentsList.add(persistenceDocumentList);
        PersistenceDocument persistenceDocument = PersistenceDocument.builder().data(documentsList).build();
        List<PersistenceDocument> documents = new ArrayList<>();
        documents.add(persistenceDocument);

        doNothing().when(microServiceRestRepository).sendPersistenceDocumentRetriesInfo(anyList());
        persistenceQueueUseCase.sendRetries(documents);
        verify(persistenceQueueUseCase, times(1)).sendRetries(documents);
    }

    @Test
    public void sendRetriesEmptyTest(){
        List<PersistenceDocument> documents = new ArrayList<>();
        doNothing().when(microServiceRestRepository).sendPersistenceDocumentRetriesInfo(anyList());
        persistenceQueueUseCase.sendRetries(documents);
        verify(persistenceQueueUseCase, times(1)).sendRetries(documents);
    }
}
