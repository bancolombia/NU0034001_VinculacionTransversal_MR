package co.com.bancolombia.model.token.validatetoken;

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
public class ValidateTokenRequest extends Request {

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private ValidateTokenRequestData data;

    public ValidateTokenRequest(MetaRequest metaRequest, ValidateTokenRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
