package co.com.bancolombia.api.model.clauseinstructions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

/**
 * InstructionsActionsResponseData
 */
@Validated
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstructionsActionsResponseData {

    @JsonProperty("actionCode")
    @ApiModelProperty(example = "ST001", value = "")
    @Size(min = 1, max = 10)
    private String actionCode;

    @JsonProperty("actionName")
    @ApiModelProperty(example = "STPNAME", value = "")
    @Size(min = 1, max = 50)
    private String actionName;
}
