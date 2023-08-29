package co.com.bancolombia.model.sqs;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class SqsMessage extends Auditing {
	private UUID id;
	private String operation;
	private String typeAcquisition;
    private Acquisition acquisition;
    private String documentType;
    private String documentNumber;
    private String request;
    private String response;
    private String message;
    private String statusMessage;
    private String helpField;
    private Integer sqsRetries;
    private Integer sqsMaxRetries;
}
