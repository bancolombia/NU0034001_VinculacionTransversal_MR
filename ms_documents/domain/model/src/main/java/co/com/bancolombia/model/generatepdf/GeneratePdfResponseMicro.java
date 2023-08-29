package co.com.bancolombia.model.generatepdf;

import co.com.bancolombia.commonsvnt.api.model.util.Meta;
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
public class GeneratePdfResponseMicro {
    private Meta meta;
    private GeneratePdfResponseData data;
}
