package co.com.bancolombia.api.model.retrievescenario;

import co.com.bancolombia.api.model.validatestatus.ValidateStatusResponseData;
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
 * RetrieveScenarioResponse
 */
@Getter
@ToString
@Validated
@Data
@Builder
public class RetrieveScenarioResponse {

    @JsonProperty("meta")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private MetaResponse meta;

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private RetrieveScenarioResponseData data;
}
