package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.model.persistencedocument.TdcDocument;

public interface PersistenceDocumentValidationUseCase {

    PersistenceDocument persistenceDocumentTDC(TdcDocument tdcDocument, String user, boolean retries);

    PersistenceDocumentList save(PersistenceDocumentList persistenceDocument);
}
