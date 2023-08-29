package co.com.bancolombia.model.persistencedocument;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PersistenceDocumentError {
    String code;
    String description;
    String documentalSubTypeCode;
}
