package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.persistencedocument.TdcDocument;

public interface PersistenceQueueUseCase {

    void sendMessagesToQueue(TdcDocument tdcDocument);
    void retrieveMessages();
}
