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
public class Answer {
    @JsonProperty("identifier")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 3)
    private String identifier;

    @JsonProperty("text")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 80)
    private String text;
}

