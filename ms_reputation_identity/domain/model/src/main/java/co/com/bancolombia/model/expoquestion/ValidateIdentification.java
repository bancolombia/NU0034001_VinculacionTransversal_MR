package co.com.bancolombia.model.expoquestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentification {

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
    private List<AlertIdentification> alertsValidateIdentification;
    private String processResult;
    private String resultCodeValidation;
    private String causeCodeValidation;
    private String inquiriesAvailable;
}
