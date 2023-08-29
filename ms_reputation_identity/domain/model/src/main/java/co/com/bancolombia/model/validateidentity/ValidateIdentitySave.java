package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentitySave {
    private String id;
    private String idAcquisition;
    private String idType;
    private String dateConsulted;
    private String economicActivity;
    private String rut;
    private String idCiiu;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String fullName;
    private String gender;
    private String maritalStatus;
    private String checkInformation;
    private String status;
    private Date issueDate;
    private String city;
    private String department;
    private String idNumber;
    private String ageMin;
    private String ageMax;
    private String antiquity;
    private String messageId;
    private Date requestDate;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private List<ValidateIdentityPhones> phones;
    private List<ValidateIdentityMobiles> mobiles;
    private List<ValidateIdentityEmails> emails;
    private List<ValidateIdentityAddresses> addresses;
    private List<ValidateIdentityAlerts> alerts;
}
