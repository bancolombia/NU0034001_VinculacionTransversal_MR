package co.com.bancolombia.model.expoquestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentificationSave {

    private String validationIdentifier;
    private String identificationNumber;
    private String identificationType;
    private String dateOfIssue;
    private String names;
    private String surnameValidation;
    private String secondSurnameValidation;
    private String namesValidation;
    private String dateOfIssueValidation;
    private String excludeCustomerParametrized;
    private String numberParameterizedAttemps;
    private String processResult;
    private String resultCodeValidation;
    private String causeCodeValidation;
    private String inquiriesAvailable;
    private String messageId;
    private Date requestDate;
    private Date createdDate;
    private Date updatedDate;
    private String updatedBy;
    private List<ValidateIdentificationAlert> alerts;
}
