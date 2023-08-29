package co.com.bancolombia.api.model.validatelegalrep;

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
public class ValidateLegalRepResponseData {
    @Valid
    @Size(min=1,max=2)
    @JsonProperty("validationCode")
    private String validationCode;

    @Valid
    @Size(min=12,max=40)
    @JsonProperty("validationDescription")
    private String validationDescription;
}
