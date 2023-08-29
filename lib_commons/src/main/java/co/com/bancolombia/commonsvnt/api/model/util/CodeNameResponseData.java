package co.com.bancolombia.commonsvnt.api.model.util;

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
public class CodeNameResponseData {

    @JsonProperty("code")
    @ApiModelProperty(example = "200", value = "")
    @Size(min=1,max=10)
    private String code;

    @ApiModelProperty(example = "Status OK", value = "")
    @Size(min=1,max=40)
    @JsonProperty("name")
    private String name;
}
