package co.com.bancolombia.api.model.clauseinstructions;

import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * InstructionsResponse
 */
@Validated
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstructionsResponse {
    @JsonProperty("meta")
    @ApiModelProperty(value = "")
    @Valid
    private MetaResponse meta;

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @Valid
    private InstructionsResponseData data;

}
