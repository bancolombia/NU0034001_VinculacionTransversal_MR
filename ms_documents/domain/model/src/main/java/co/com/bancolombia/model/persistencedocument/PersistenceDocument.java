package co.com.bancolombia.model.persistencedocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class PersistenceDocument {
    private PersistenceDocumentResponse persistenceDocumentResponse;
    private PersistenceDocumentError persistenceDocumentError;
    private InfoReuseCommon infoReuseCommon;
    private List<PersistenceDocumentList> data;
    private String documentType;
    private Date initOperation;
}
