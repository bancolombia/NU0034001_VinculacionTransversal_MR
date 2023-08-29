package co.com.bancolombia.api.model.acquisition;

import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * AcquisitionResponse
 */
@Validated
@Builder(toBuilder = true)
public class AcquisitionResponse {
    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @Valid
    private AcquisitionResponseData data;

    @JsonProperty("meta")
    @ApiModelProperty(value = "")
    @Valid
    private MetaResponse meta;
}
