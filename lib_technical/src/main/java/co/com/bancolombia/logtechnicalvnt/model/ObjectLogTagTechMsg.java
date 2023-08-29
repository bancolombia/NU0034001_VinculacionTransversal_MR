package co.com.bancolombia.logtechnicalvnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectLogTagTechMsg {
	@JsonProperty(value = "env")
	private String env;
}
