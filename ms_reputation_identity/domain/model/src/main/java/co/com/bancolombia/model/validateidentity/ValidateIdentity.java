package co.com.bancolombia.model.validateidentity;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentity {

    private String outComeCode;
    private String outComeName;
    private Double matchPercentaje;
    private Boolean documentManual;
    private Boolean emailAndCellError;

    private InfoReuseCommon infoReuseCommon;
}
