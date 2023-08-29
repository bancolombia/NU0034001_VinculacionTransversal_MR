package co.com.bancolombia.model.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecretModelApiOnPremise {
    private String clientid;
    private String clientsecret;
}
