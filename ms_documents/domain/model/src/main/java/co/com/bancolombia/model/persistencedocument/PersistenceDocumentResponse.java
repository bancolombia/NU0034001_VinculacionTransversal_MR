package co.com.bancolombia.model.persistencedocument;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PersistenceDocumentResponse {
    private String idDocument;
}
