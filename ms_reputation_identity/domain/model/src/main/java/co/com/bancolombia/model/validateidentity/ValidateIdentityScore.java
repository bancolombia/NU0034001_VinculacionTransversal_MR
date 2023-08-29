package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityScore {

    private String id;
    private String idAcquisition;
    private BigDecimal ruleOneCellphone;
    private BigDecimal ruleTwoEmail;
    private BigDecimal ruleThreeAge;
    private BigDecimal ruleFourDateExpedition;
    private BigDecimal ruleFiveFullName;
    private BigDecimal ruleSixSecondSurname;
    private BigDecimal ruleSevenWorkPhone;
    private BigDecimal ruleEightWorkCellphone;
    private BigDecimal ruleNineWorkEmail;
    private BigDecimal ruleTenNames;
    private BigDecimal accumulated;
}
