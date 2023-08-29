package co.com.bancolombia.api.model.validatestatus;

import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * ValidateStatusResponse
 */
@Getter
@ToString
@Validated
@Data
@Builder
public class ValidateStatusResponse {
    @JsonProperty("meta")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private MetaResponse meta;

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private ValidateStatusResponseData data;
}
