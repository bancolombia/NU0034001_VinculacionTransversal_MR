package co.com.bancolombia.model.validatesession;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ValidateSessionRequest {
    @JsonProperty("grant_type")
    private String grantType;

    private String username;
    private String password;
    private String scope;
}
