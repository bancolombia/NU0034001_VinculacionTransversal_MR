package co.com.bancolombia.model.validateidentity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityRequest {
    private String firstSurname;
    private String document;
}
