package co.com.bancolombia.logfunctionalvnt.log.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Builder(toBuilder = true)
@Data
public class LogObjectAttribute {
    
    private Map<String, List<String>> fieldNotSve;
    private Date dateInit;
    private String createdBy;

    private String documentType;
    private String documentNumber;
    private String acquisitionId;
    
    private String stateOperation;
    private String stateAcquisition;
    
    private String consumedResponseServiceCode;
    private String consumedResponseServiceTitle;
    private String consumedResponseServiceDetail;
    
    private String consumerResponseServiceCode;
    private String consumerResponseServiceTitle;
    private String consumerResponseServiceDetail;    

}
