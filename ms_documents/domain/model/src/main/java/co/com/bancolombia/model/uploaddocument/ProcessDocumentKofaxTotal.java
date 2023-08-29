package co.com.bancolombia.model.uploaddocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProcessDocumentKofaxTotal {
    private KofaxInformation kofaxInformation;
    private KofaxRutInformation kofaxRutInformation;
    private ProcessDocumentKofaxError processDocumentKofaxError;
}
