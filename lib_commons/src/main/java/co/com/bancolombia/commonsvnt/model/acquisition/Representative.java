package co.com.bancolombia.commonsvnt.model.acquisition;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Representative extends Auditing {
    private String tyDocumentRepresentative;
    private String documentNumberRepresentative;
}
