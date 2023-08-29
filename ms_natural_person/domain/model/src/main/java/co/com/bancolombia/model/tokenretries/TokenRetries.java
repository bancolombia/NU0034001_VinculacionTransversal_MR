package co.com.bancolombia.model.tokenretries;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TokenRetries extends Auditing {

    private UUID id;
    private Integer generateTokenRetries;
    private Date generateTokenLockDate;
    private Integer validateTokenRetries;
    private Date validateTokenLockDate;
    private Acquisition acquisition;
}
