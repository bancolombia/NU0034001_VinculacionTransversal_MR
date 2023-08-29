package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;

import java.util.List;

public interface PersistenceValidateDocUseCase {

    public String validateError(PersistenceDocumentList persistenceDocument, AcquisitionReply acquisition,
                                String typeDocument);

    public void validateDocument(List<PersistenceDocumentList> data, AcquisitionReply acquisition, String codeDocument,
                                 String typeDocument);
}
