package co.com.bancolombia.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
@AllArgsConstructor
@Builder
@Data
public class ValidateQuestionResponseData {

    @JsonProperty("outComeCode")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 2)
    private String outComeCode;

    @ApiModelProperty(example = "Validación de preguntas exitosa, "
            + "es posible continuar con el proceso de vinculación", value = "")
    @Size(min = 1, max = 60)
    @JsonProperty("outComeName")
    private String outComeName;
}
