package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsUplDocApiResponse {
    private SqsUplDocApiResponseData data;
}
