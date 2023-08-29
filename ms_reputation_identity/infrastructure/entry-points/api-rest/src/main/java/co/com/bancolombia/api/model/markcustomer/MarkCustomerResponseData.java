package co.com.bancolombia.api.model.markcustomer;

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
public class MarkCustomerResponseData {

    @JsonProperty("answerName")
    @Size(min = 1, max = 60)
    @ApiModelProperty(example = "1", value = "")
    private String answerName;

    @JsonProperty("answerCode")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 2)
    private String answerCode;
}
