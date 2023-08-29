package co.com.bancolombia.model.token.generatetoken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Validated
@AllArgsConstructor
@Builder
@Data
public class GenerateTokenResponseData {
    @Valid
    @Size(min=1,max=2)
    @JsonProperty("answerCode")
    private String answerCode;

    @Valid
    @Size(min=10,max=40)
    @JsonProperty("answerName")
    private String answerName;
}
