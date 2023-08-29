package co.com.bancolombia.model.persistencedocument.gateways;

import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;

import java.util.concurrent.Future;

public interface DocumentPersistenceRestRepository {

    Future<PersistenceDocument> getPersistence(TdcDocument tdcDocument, TdcDocumentsFile tdcDocumentsFile,
                                               String base64, String rutExpeditionDate);
}
