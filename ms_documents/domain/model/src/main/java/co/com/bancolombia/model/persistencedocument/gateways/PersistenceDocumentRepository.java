package co.com.bancolombia.model.persistencedocument.gateways;

import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;

public interface PersistenceDocumentRepository {

    PersistenceDocumentList save(PersistenceDocumentList persistenceDocument);
}
