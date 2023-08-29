package co.com.bancolombia.model.generatetoken.reuseresponse;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GTResponseWithLog {
    private GTResponseOk generateTokenResponse;
    private InfoReuseCommon infoReuseCommon;
}