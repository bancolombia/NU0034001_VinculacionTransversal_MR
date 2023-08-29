package co.com.bancolombia.api.model.acceptclauses;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.Request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * AcceptClausesRequest
 */
@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AcceptClausesRequest extends Request {

    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @Valid
    @NotNull
    private AcceptClausesRequestData data;

    public AcceptClausesRequest(MetaRequest metaRequest, AcceptClausesRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
