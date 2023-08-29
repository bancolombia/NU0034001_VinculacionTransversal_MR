package co.com.bancolombia.model.markcustomer;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
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
public class MarkCustomerResponseWithLog {
    private InfoReuseCommon infoReuseCommon;
    private RegisterControlListTotal controlListResponse;
}
