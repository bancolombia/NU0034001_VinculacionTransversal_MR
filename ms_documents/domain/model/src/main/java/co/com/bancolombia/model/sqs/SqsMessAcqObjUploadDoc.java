package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsMessAcqObjUploadDoc {
	private String documentNumber;
	private String id;
	private int uploadDocumentRetries;
	private String documentType;

}
