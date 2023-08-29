package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;

import java.util.List;

public interface PersistenceValidationsRetriesUseCase {

    public String validateError(PersistenceDocumentList persistenceDocument);

    public void validateDocument(List<PersistenceDocumentList> data, AcquisitionReply acquisition,
                                 String codeDocument);

    public void markState(PersistenceDocumentList persistenceDocument, AcquisitionReply acquisition, String stateAux);

    public void deleteFile(String keyFile);
}
