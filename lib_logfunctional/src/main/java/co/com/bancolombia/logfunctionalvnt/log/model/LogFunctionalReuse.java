package co.com.bancolombia.logfunctionalvnt.log.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LogFunctionalReuse {
	private String requestReuse;
	private String responseReuse;
	private Date dateRequest;
	private Date dateResponse;
}
