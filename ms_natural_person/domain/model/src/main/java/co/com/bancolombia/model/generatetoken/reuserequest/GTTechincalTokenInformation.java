package co.com.bancolombia.model.generatetoken.reuserequest;

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
public class GTTechincalTokenInformation {
    private String sendToken;
    private String serverId;
    private String sourceSystemId;
    private GTTechTokenInfo techTokenInfo;
}