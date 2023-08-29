package co.com.bancolombia.logtechnicalvnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ObjectLogErrorTechMsg {
	private String id;
	private String transactionId;
	private String name;
	private Timestamp timestamp;
	private Integer timemillis;
	private String application;
	private String service;
	private String component;
	private String level;
	@JsonProperty(value = "tag")
	private ObjectLogTagTechMsg tag;
	private ObjectLogMessageTechMsg message;
}

