package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityMobiles {

    private String entities;
    private String lastConfirmation;
    private String typee;
    private String source;
    private Date creationDate;
    private Date updateDate;
    private String idMobile;
    private String mobile;
    private String reporting;
    private String numberReports;
}
