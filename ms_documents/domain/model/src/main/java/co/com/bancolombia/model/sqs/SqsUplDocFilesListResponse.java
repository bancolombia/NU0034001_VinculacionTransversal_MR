package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsUplDocFilesListResponse {
    private String documentalTypeCode;
    private String documentalSubTypeCode;
    private String fileName;
    private String outComeCode;
    private String outComeName;
    private String reason;
}
