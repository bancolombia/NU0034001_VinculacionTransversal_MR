package co.com.bancolombia.model.generatetoken.reuseresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GTResponseOk {
    private GTMeta meta;
    private GTDatum data;
}
