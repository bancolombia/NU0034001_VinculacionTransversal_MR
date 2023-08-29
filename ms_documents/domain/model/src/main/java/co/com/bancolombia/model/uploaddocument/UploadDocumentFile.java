package co.com.bancolombia.model.uploaddocument;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadDocumentFile {
    private String documentalTypeCode;
    private String documentalSubTypeCode;
    private String fileName;
    private String flagDataExtraction;
    private String flagSynchronous;
    private String documentnumber;
    private String documentType;
}
