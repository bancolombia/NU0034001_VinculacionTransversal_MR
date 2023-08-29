package co.com.bancolombia.model.generatetoken.reuserequest;

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
public class GTRequestItem {
    private GTTechincalTokenInformation techincalTokenInformation;
    private GTCustomerIdentification customerIdentification;
    private GTAccountInformation accountInformation;
    private GTCellphoneInformation cellphoneInformation;
    private GTEmailInformation emailInformation;
}