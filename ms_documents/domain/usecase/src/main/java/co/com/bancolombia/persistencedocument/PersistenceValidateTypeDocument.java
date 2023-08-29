package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.model.persistencedocument.TdcDocument;
import co.com.bancolombia.model.persistencedocument.TdcDocumentsFile;

import java.io.IOException;

public interface PersistenceValidateTypeDocument {

    public String getBase64DiffExtension(TdcDocumentsFile tdcDocumentsFile,
                                         TdcDocument tdcDocument) throws IOException;
}
