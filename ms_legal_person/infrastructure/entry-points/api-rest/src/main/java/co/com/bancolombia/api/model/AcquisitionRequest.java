package co.com.bancolombia.api.model;

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
public class AcquisitionRequest {

    @JsonProperty("data")
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Valid
    private AcquisitionRequestUser data;

    public AcquisitionRequest(AcquisitionRequestUser data) {
        this.data = data;
    }
}
