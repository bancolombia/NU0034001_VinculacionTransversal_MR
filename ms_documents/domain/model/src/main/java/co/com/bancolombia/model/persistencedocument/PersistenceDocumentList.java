package co.com.bancolombia.model.persistencedocument;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class PersistenceDocumentList extends Auditing {
    private UUID id;
    private UUID acquisitionId;
    private String typeDocumentary;
    private String subTypeDocumentary;
    private String documentNumber;
    private List<String> fileNameOriginal;
    private String idDocument;
    private String errorCode;
    private String errorDescription;
    private String status;
}
