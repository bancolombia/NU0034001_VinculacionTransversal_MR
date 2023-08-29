package co.com.bancolombia.commonsvnt.api.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Builder
@NoArgsConstructor
@Validated
@AllArgsConstructor
public class CodeNameResponse {

    @JsonProperty("meta")
    @ApiModelProperty(value = "")
    @Valid
    private MetaResponse meta;

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @Valid
    private CodeNameResponseData data;

}
