package co.com.bancolombia.model.signdocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SDRequestItem {

    private String documentType;
    private String documentNumber;
    private String digitalSignature;
    private SDDocument document;
}
