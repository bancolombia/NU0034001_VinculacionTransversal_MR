package co.com.bancolombia.logtechnicalvnt.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ObjectLogMessageTechMsg {
	private String description;
	private String method;
	private String inputData;
	private List<ObjectLogStacktraceTechMsg> stackTrace;
}
