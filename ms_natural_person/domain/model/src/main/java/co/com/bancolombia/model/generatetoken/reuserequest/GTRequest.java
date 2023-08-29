package co.com.bancolombia.model.generatetoken.reuserequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GTRequest {
    private GTRequestItem data;
}
