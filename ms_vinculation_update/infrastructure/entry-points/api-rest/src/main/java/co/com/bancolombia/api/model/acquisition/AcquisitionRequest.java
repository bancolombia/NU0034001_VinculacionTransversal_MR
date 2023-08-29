package co.com.bancolombia.api.model.acquisition;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.Request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * AcquisitionRequest
 */
@Validated
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AcquisitionRequest extends Request {

    @JsonProperty("data")
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Valid
    private AcquisitionRequestUser data;

    public AcquisitionRequest(AcquisitionRequestUser data, MetaRequest metaRequest) {
        super(metaRequest);
        this.data = data;
    }
}
