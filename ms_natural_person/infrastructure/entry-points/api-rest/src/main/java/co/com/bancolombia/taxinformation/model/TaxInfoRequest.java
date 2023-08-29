package co.com.bancolombia.taxinformation.model;

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

/**
 * TaxInfoRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaxInfoRequest extends Request {
    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private TaxInfoRequestData data;

    public TaxInfoRequest(MetaRequest metaRequest, TaxInfoRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}