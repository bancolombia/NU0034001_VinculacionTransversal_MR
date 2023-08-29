package co.com.bancolombia.api.model;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.Request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ValidateQuestionsRequest extends Request {

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private ValidateQuestionsRequestData data;

    public ValidateQuestionsRequest(MetaRequest metaRequest, ValidateQuestionsRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
