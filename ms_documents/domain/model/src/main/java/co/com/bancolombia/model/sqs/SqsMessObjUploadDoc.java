package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsMessObjUploadDoc {
	private SqsMessDataObjUploadDoc data;
	private SqsMetaUploadDocument meta;
}
