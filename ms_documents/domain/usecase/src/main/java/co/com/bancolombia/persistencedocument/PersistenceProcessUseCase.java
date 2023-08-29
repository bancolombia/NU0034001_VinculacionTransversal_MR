package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;

public interface PersistenceProcessUseCase {

    void processResponse(AcquisitionReply acquisition, PersistenceDocument persistenceDocumentWithLog);
}
