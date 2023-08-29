package co.com.bancolombia.model.documentretries;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class DocumentRetries extends Auditing {
    private UUID id;
    private Integer uploadDocumentRetries;
    private Integer uploadRutRetries;
    private Acquisition acquisition;
}
