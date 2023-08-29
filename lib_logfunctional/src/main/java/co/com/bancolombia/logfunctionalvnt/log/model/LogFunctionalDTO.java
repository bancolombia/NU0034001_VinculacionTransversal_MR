package co.com.bancolombia.logfunctionalvnt.log.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFunctionalDTO {
    private UUID id;
    private String documentType;
    private String documentNumber;
    private String acquisitionId;

    private String request;
    private String response;

    private String manangment;
    private String operation;
    private Date dateInitOperation;
    private Date dateFinalOperation;
    private String stateOperation;
    private String stateAcquisition;
    
    
    private int consecutive;
    private String ipAddress;

    private Date createdDate;
    private String createdBy;
    
    private String timeZone;

    private String requestReuse;
    private Date requestDateReuse;
    private String responseReuse;
    private Date responseDateReuse;
}
