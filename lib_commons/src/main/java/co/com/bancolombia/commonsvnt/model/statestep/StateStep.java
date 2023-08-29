package co.com.bancolombia.commonsvnt.model.statestep;


import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class StateStep  extends Auditing {
    private UUID id;
    private String code;
    private String name;

}
