package co.com.bancolombia.model.persistencedocument.gateways;

import co.com.bancolombia.model.persistencedocument.PersistenceDocument;

import java.util.List;

public interface ServicePersistenceRestRepository {

    void sendPersistenceDocumentRetriesInfo(List<PersistenceDocument> documentWithLogList);
}
