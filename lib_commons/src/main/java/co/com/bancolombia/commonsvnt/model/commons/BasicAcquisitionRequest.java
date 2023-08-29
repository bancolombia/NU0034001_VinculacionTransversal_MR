package co.com.bancolombia.commonsvnt.model.commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class BasicAcquisitionRequest {
	private String idAcq;
	private String documentType;
	private String documentNumber;
	private String userTransaction;
	private String messageId;
}
