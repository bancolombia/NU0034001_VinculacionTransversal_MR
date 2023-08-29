package co.com.bancolombia.logtechnicalvnt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectLogStacktraceTechMsg {
	private String classs;
	private String method;
	private String file;
	private String line;
}
