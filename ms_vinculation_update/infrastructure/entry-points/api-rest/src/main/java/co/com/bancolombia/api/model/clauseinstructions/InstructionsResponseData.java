package co.com.bancolombia.api.model.clauseinstructions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * InstructionsResponseData
 */
@Validated
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstructionsResponseData {

    @JsonProperty("clausesList")
    @ApiModelProperty(value = "")
    @Valid
    private List<InstructionsClausesResponseData> clausesList;

    @JsonProperty("instructionsList")
    @ApiModelProperty(value = "")
    @Valid
    private List<Instruction> instructionsList;

    @JsonProperty("informationProcessingUrl")
    @ApiModelProperty(value = "")
    @Valid
    private String informationProcessingUrl;

    @JsonProperty("dataAdministrationPolicyUrl")
    @ApiModelProperty(value = "")
    @Valid
    private String dataAdministrationPolicyUrl;

}
