package co.com.bancolombia.commonsvnt.model.commons.secretsmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecretModelApiKeyInt {
    private String apiKeyInt;
}
