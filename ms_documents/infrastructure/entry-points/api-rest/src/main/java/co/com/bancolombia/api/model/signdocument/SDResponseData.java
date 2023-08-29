package co.com.bancolombia.api.model.signdocument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Validated
@AllArgsConstructor
@Builder
@Data
public class SDResponseData {

    @Valid
    @Size(min=1,max=2)
    @JsonProperty("responseCode")
    private String responseCode;

    @Valid
    @JsonProperty("responseName")
    private String responseName;
}
