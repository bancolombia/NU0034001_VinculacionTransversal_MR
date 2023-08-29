package co.com.bancolombia.economicinformation.model;

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
public class EconomicInfoResponseData {
    @JsonProperty("answerCode")
    @ApiModelProperty(example = "00", value = "")
    @Size(min=1,max=2)
    private String answerCode;

    @ApiModelProperty(example = "Exitoso. NO requiere cargar RUT", value = "")
    @Size(min=1,max=60)
    @JsonProperty("answerName")
    private String answerName;
}
