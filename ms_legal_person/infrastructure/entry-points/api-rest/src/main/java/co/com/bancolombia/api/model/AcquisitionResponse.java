package co.com.bancolombia.api.model;

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
}
