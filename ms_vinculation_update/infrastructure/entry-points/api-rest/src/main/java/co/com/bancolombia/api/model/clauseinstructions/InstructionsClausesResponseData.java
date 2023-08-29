package co.com.bancolombia.api.model.clauseinstructions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * InstructionsClausesResponseData
 */
@Validated
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstructionsClausesResponseData {

    @JsonProperty("code")
    @ApiModelProperty(example = "001", value = "")
    @Size(min = 1, max = 10)
    private String code;

    @ApiModelProperty(example = "Aceptas clausulas Bancolombia PN", value = "")
    @Size(min = 1, max = 1000)
    @JsonProperty("name")
    private String name;

    @JsonProperty("order")
    @ApiModelProperty(example = "1", value = "1")
    private Integer order;

    @JsonProperty("stepCode")
    @ApiModelProperty(example = "ST001", value = "")
    @Size(min = 1, max = 10)
    private String stepCode;

    @JsonProperty("stepName")
    @ApiModelProperty(example = "STPNAME", value = "")
    @Size(min = 1, max = 100)
    private String stepName;

    @JsonProperty("actionsList")
    @ApiModelProperty(value = "")
    @Valid
    private List<InstructionsActionsResponseData> actionsList;
}
