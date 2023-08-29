package co.com.bancolombia.api.model.expoquestion;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
@AllArgsConstructor
@Builder
public class Question {
    @JsonProperty("identifier")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 9)
    private String identifier;

    @JsonProperty("text")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 280)
    private String text;

    @JsonProperty("order")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 3)
    private String order;

    @JsonProperty("correctAnswerId")
    private String correctAnswerId;

    @JsonProperty("weight")
    private Integer weight;
}
