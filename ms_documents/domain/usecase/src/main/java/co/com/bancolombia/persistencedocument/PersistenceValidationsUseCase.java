package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;

public interface PersistenceValidationsUseCase {

    public void markState(PersistenceDocumentList persistenceDocument, AcquisitionReply acquisition, String state);

    public void deleteFile(String keyFile);
}
