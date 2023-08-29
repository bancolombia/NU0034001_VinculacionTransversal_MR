package co.com.bancolombia.economicinformation.model;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.Request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * EconomicInfoRequest
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EconomicInfoRequest extends Request {
    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private EconomicInfoRequestData data;

    public EconomicInfoRequest(MetaRequest metaRequest, EconomicInfoRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
