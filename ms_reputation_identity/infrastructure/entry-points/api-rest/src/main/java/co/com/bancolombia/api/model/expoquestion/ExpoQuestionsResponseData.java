package co.com.bancolombia.api.model.expoquestion;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@AllArgsConstructor
@Builder
@Data
public class ExpoQuestionsResponseData {
    @JsonProperty("questionnaireId")
    @ApiModelProperty(example = "1", value = "")
    @Size(min=1,max=10)
    private String questionnaireId;

    @JsonProperty("questionnaireRecordId")
    @ApiModelProperty(example = "1", value = "")
    @Size(min=1,max=10)
    private String questionnaireRecordId;

    @JsonProperty("questionsList")
    @ApiModelProperty(value = "")
    @Valid
    private List<Question> questionsList;

    @JsonProperty("answersList")
    @ApiModelProperty(value = "")
    @Valid
    private List<Answer> answersList;
}
