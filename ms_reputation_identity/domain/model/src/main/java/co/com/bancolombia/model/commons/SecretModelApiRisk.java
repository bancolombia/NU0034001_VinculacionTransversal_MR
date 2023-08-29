package co.com.bancolombia.model.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecretModelApiRisk {
    private String userId;
    private String productVersion;
    private String parameterCode;
}
