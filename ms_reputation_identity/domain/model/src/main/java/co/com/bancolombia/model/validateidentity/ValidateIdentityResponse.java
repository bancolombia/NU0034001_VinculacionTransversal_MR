package co.com.bancolombia.model.validateidentity;

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
public class ValidateIdentityResponse {
    private Meta meta;
    private ValidateIdentityItemsResponse data;
}
