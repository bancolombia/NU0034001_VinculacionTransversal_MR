package co.com.bancolombia.commonsvnt.model.basicinfo;

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
 * BasicInfoRequest
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BasicInfoRequest extends Request {
    @JsonProperty("data")
    @ApiModelProperty(value = "")
    @NotNull
    @Valid
    private BasicInfoRequestData data;

    public BasicInfoRequest(MetaRequest metaRequest, BasicInfoRequestData data) {
        super(metaRequest);
        this.data = data;
    }
}
