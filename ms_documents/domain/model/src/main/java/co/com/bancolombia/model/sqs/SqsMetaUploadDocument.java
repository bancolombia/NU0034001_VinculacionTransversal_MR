package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqsMetaUploadDocument {
	private String usrMod;
	private String ip; 
	private String systemId;
	private String messageId;
	private String version;
	private String requestDate;
	private String service;
	private String operation;
}
