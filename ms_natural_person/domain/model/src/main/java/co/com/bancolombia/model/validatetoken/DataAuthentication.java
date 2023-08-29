package co.com.bancolombia.model.validatetoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAuthentication {
    private String serverId;
    private String sourceSystemId;
}
