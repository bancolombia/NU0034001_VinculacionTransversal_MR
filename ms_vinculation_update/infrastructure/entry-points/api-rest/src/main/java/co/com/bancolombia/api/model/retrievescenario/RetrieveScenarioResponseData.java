package co.com.bancolombia.api.model.retrievescenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * RetrieveScenarioResponseData
 */
@Validated
@Data
@Builder
public class RetrieveScenarioResponseData {

    @JsonProperty("acquisitionCode")
    @ApiModelProperty(example = "VT001", value = "")
    @Size(min = 1, max = 20)
    @Valid
    private String acquisitionCode;

    @JsonProperty("acquisitionName")
    @ApiModelProperty(example = "Vinculación Completa", value = "")
    @Size(min = 1, max = 60)
    @Valid
    private String acquisitionName;

    @JsonProperty("generateTokenMaxRetries")
    @ApiModelProperty(example = "2", value = "")
    @Size(min = 1, max = 2)
    @Valid
    private String generateTokenMaxRetries;

    @JsonProperty("validateTokenMaxRetries")
    @ApiModelProperty(example = "2", value = "")
    @Size(min = 1, max = 2)
    @Valid
    private String validateTokenMaxRetries;

    @JsonProperty("acquisitionStepsList")
    @ApiModelProperty(value = "")
    @Valid
    private List<RetrieveScenarioResponseDataAcquisitionStepsList> acquisitionStepsListList;
}
