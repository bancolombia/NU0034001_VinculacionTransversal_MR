package co.com.bancolombia.model.validatetoken;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ValidateTokenRequest {
    private List<ValidateTokenRequestItem> data;
}
