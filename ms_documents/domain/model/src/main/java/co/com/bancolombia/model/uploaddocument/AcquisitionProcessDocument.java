package co.com.bancolombia.model.uploaddocument;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AcquisitionProcessDocument {
    private UUID id;
    private String documentNumber;
    private Integer uploadDocumentRetries;
    private Integer uploadRutRetries;

}
