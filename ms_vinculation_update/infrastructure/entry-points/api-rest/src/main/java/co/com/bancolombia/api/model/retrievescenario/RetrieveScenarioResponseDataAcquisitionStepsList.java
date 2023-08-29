package co.com.bancolombia.api.model.retrievescenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * RetrieveScenarioResponseDataAcquisitionStepsList
 */
@Validated
@Data
@Builder
public class RetrieveScenarioResponseDataAcquisitionStepsList {

    @JsonProperty("stepCode")
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Size(min = 1, max = 30)
    @Valid
    private String stepCode;

    @JsonProperty("stepName")
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Size(min = 1, max = 60)
    @Valid
    private String stepName;

    @JsonProperty("order")
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Size(min = 1, max = 2)
    @Valid
    private String order;
}
