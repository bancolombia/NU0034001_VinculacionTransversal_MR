package co.com.bancolombia.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
@AllArgsConstructor
@Builder
@Data
public class ControlListResponseData {

    @JsonProperty("validationCode")
    @ApiModelProperty(example = "6", value = "")
    @Size(min = 1, max = 2)
    private String validationCode;

    @ApiModelProperty(example = "No está en Listas de Control", value = "")
    @Size(min = 1, max = 60)
    @JsonProperty("validationName")
    private String validationName;
}
