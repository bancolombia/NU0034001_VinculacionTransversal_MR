package co.com.bancolombia.model.parameters;

import co.com.bancolombia.common.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Parameters extends Auditing {
    private UUID id;
    private String code;
    private String name;
    private String parent;
}