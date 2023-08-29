package co.com.bancolombia.model.uploaddocument;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProcessDocumentFiles {
    private String fileName;
    private String base64;
    private String extension;
    private String documentalSubTypeCode;
    private String documentalTypeCode;
}
