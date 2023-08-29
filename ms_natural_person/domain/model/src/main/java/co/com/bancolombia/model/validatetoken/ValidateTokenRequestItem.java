package co.com.bancolombia.model.validatetoken;

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
public class ValidateTokenRequestItem {
    private DataAuthentication dataAuthentication;
    private CustomerIdentification customerIdentification;
    private CellphoneInformation cellphoneInformation;
    private TokenInformation tokenInformation;
}
