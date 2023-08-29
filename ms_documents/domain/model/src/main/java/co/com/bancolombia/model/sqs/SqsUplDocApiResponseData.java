package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SqsUplDocApiResponseData {
    private List<SqsUplDocFilesListResponse> processedDocuments;
}
