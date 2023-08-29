package co.com.bancolombia.commonsvnt.model.secretsmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecretModelRabbit {
    private String username;
    private String password;
}
