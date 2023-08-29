package co.com.bancolombia.model.markcustomer;

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
public class RegisterGenericErrorResponse {
    private String httpCode;
    private String httpMessage;
    private String moreInformation;
}
