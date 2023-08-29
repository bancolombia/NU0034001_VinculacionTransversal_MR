package co.com.bancolombia.commonsvnt.model.validateidentityrules;

import java.math.BigDecimal;
import java.util.UUID;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityRules extends Auditing {
    private UUID id;
    private boolean active;
    private String rule;
    private String name;
    private BigDecimal score;
    private TypeAcquisition typeAcquisition;
}

