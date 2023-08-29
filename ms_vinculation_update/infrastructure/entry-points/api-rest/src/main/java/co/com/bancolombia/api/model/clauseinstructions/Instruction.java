package co.com.bancolombia.api.model.clauseinstructions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

/**
 * Instruction
 */
@Validated
@AllArgsConstructor
@Builder
public class Instruction {
    @JsonProperty("code")
    @ApiModelProperty(example = "124342321", value = "")
    @Size(min = 1, max = 10)
    private String code;

    @JsonProperty("name")
    @ApiModelProperty(example = "Carga documentos cliente", value = "")
    @Size(min = 1, max = 300)
    private String name;

    @JsonProperty("stepName")
    @ApiModelProperty(example = "STPNAME", value = "")
    @Size(min = 1, max = 25)
    private String stepName;

    @JsonProperty("stepCode")
    @ApiModelProperty(example = "ST001", value = "")
    @Size(min = 1, max = 10)
    private String stepCode;

    @JsonProperty("order")
    @ApiModelProperty(example = "1", value = "1")
    private Integer order;

}
