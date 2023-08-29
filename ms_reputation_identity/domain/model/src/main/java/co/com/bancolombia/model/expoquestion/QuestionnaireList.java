package co.com.bancolombia.model.expoquestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireList {
    private String userId;
    private String productVersion;
    private String parameterCode;
    private String identificationNumber;
    private String identificationType;
    private String validationIdentifier;
}
