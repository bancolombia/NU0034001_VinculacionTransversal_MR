package co.com.bancolombia.model.controllist;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ControlList {

    private String validationName;
    private String validationCode;

    private InfoReuseCommon infoReuseCommon;
}
