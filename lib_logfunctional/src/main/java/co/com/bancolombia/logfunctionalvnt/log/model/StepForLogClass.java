package co.com.bancolombia.logfunctionalvnt.log.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class StepForLogClass {
    private UUID idAcquisition;
    private String operation;
    private String requestReuse; 
    private Date requestDateReuse;
    private String responseReuse;
    private Date responseDateReuse;
    private Map<String, String> mapaField;
}
