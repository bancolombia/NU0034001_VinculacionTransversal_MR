package co.com.bancolombia.model.signdocument;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TxtConstruction {

    private UUID id;
    private String value;
    private String iteration;
    private String iterationOrder;
}
