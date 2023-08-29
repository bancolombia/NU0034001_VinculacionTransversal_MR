package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityEmails {

    private String email;
    private String lastConfirmation;
    private String entities;
    private String source;
    private String idEmail;
    private Date creationDate;
    private Date updateDate;
    private String reporting;
    private String numberReports;
}
