package co.com.bancolombia.api.model.acquisition;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * AcquisitionResponseData
 */
@Validated
@Builder(toBuilder = true)
public class AcquisitionResponseData {
    @JsonProperty("acquisitionId")
    @ApiModelProperty(value = "")
    @Size(max = 40)
    private String acquisitionId;

    @JsonProperty("documentType")
    @ApiModelProperty(value = "")
    @Valid
    @Size(min = 5, max = 5)
    private String documentType;

    @JsonProperty("documentNumber")
    @ApiModelProperty(value = "")
    @Size(min = 1, max = 20)
    private String documentNumber;

    @JsonProperty("registrationDate")
    @ApiModelProperty(example = "20200421", value = "")
    @Pattern(regexp = "^\\d{4}(0[1-9]|1[012])([0-2][0-9]|3[01])([01][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])")
    @Size(max = 8)
    private String registrationDate;

    @JsonProperty("codeStateAcquisition")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 1)
    private String codeStateAcquisition;

    @JsonProperty("nameStateAcquisition")
    @ApiModelProperty(example = "ACTIVO", value = "")
    @Size(min = 1, max = 15)
    private String nameStateAcquisition;

}
