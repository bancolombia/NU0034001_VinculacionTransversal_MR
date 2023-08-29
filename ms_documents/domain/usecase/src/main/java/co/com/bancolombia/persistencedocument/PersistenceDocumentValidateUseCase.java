package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;

public interface PersistenceDocumentValidateUseCase {

    PersistenceDocument getPersistenceDocumentWithLog(TdcDocument tdcDocument, TdcDocumentsFile tdcDocumentsFile);
}
