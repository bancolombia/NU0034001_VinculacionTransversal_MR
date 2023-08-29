package co.com.bancolombia.model.validateidentity;

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
public class ValidateIdentityTotalResponse {

    private ValidateIdentityResponse validateIdentityResponse;
    private ValidateIdentityResponseError errors;
    private InfoReuseCommon infoReuseCommon;
}
