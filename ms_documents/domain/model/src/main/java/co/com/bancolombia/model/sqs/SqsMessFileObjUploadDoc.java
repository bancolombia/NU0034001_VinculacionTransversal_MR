package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsMessFileObjUploadDoc {
	private String base64;
	private String extension;
	private String fileName;
	private String flagDataExtraction;
	private String flagSynchronous;
	private String documentalSubTypeCode;
	private String documentalTypeCode;
}
