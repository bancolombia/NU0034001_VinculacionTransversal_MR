package co.com.bancolombia.api.model.validatestatus;

import co.com.bancolombia.commonsvnt.model.activity.Activity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_UUID;

/**
 * ValidateStatusResponseDataStartList
 */
@Validated
@Data
@Builder
public class ValidateStatusResponseDataStartList {
    @JsonProperty("acquisitionId")
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Size(max = 40)
    @Pattern(regexp = REGEX_UUID)
    @Valid
    private String acquisitionId;

    @JsonProperty("documentType")
    @ApiModelProperty(value = "")
    @Size(min = 5, max = 5)
    @Valid
    private String documentType;

    @JsonProperty("documentNumber")
    @ApiModelProperty(value = "")
    @Size(min = 1, max = 20)
    @Valid
    private String documentNumber;

    @JsonProperty("registrationDate")
    @ApiModelProperty(example = "20150625200000", value = "")
    @Size(max = 19)
    @Valid
    private String registrationDate;

    @JsonProperty("codeStateAcquisition")
    @ApiModelProperty(example = "1", value = "")
    @Size(min = 1, max = 1)
    @Valid
    private String codeStateAcquisition;

    @JsonProperty("nameStateAcquisition")
    @ApiModelProperty(example = "ACTIVO", value = "")
    @Size(min = 1, max = 15)
    @Valid
    private String nameStateAcquisition;

    @JsonProperty("clausesList")
    @ApiModelProperty(value = "")
    @Valid
    private List<ValidateStatusClausesResponseData> clausesList;

    @JsonProperty("activitiesList")
    @ApiModelProperty(value = "")
    @Valid
    private List<Activity> activitiesList;
}
