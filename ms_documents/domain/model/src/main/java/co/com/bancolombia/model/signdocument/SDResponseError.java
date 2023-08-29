package co.com.bancolombia.model.signdocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class SDResponseError {

    private SDResponseMeta meta;
    private String status;
    private String title;
    private List<SDResponseErrorItem> errors;
}
