package co.com.bancolombia.model.validatequestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateQuestion {

    private String outComeCode;
    private String outComeName;

    private InfoReuseCommon infoReuseCommon;
}
