package co.com.bancolombia.api.model.expoquestion;

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
public class ExpoQuestionsRequest extends Request {
    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private ExpoQuestionsRequestData data;

    public ExpoQuestionsRequest(MetaRequest metaRequest, ExpoQuestionsRequestData data){
        super (metaRequest);
        this.data = data;
    }
}

