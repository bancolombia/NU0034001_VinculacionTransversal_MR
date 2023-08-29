package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;

public interface PersistenceDocumentUseCase {

    PersistenceDocument startProcess(String documentCode, AcquisitionReply acquisitionReply, String messageId,
                                     String user);

    void processResponse(AcquisitionReply acquisition,
                         PersistenceDocument persistenceDocumentApiResponse, boolean retries);
}
