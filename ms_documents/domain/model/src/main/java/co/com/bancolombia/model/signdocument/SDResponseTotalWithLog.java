package co.com.bancolombia.model.signdocument;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class SDResponseTotalWithLog {

    private SDResponseTotal responseTotal;
    private InfoReuseCommon infoReuseCommon;
}
