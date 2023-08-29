package co.com.bancolombia.model.generatetoken.reuseresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GTResponseError {
    private GTMeta meta;
    private List<GTErrorItem> errors;
}
