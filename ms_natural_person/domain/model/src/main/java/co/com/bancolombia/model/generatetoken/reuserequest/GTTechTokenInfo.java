package co.com.bancolombia.model.generatetoken.reuserequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GTTechTokenInfo {
    private String generateEncryptedToken;
    private String tokenValidity;
}