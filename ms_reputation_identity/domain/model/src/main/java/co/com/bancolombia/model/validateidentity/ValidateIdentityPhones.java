package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityPhones {

    private String phoneNumber;
    private String entities;
    private String department;
    private String idCountry;
    private String city;
    private String address;
    private String lastConfirmation;
    private String idAddress;
    private String idArea;
    private String useType;
    private String source;
    private Date creationDate;
    private Date updateDate;
    private String numberReports;
    private String typeResidence;
    private String typeCorrespondence;
}
