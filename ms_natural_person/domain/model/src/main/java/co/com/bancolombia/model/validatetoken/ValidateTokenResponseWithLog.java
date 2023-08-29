package co.com.bancolombia.model.validatetoken;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateTokenResponseWithLog {
    private ValidateTokenResponse validateTokenResponse;
    private InfoReuseCommon infoReuseCommon;
}