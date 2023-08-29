package co.com.bancolombia.model.validatesession;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateSession extends Auditing {

    private UUID id;
    private String tokenType;
    private String accessToken;
    private String expiresIn;
    private String consentedOn;
    private String scope;
    private String refreshToken;
    private String refreshTokenExpiresIn;
    private Acquisition acquisition;
}
