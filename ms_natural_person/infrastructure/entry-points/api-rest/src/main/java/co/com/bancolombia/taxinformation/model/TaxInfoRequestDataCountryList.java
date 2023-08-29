package co.com.bancolombia.taxinformation.model;

import co.com.bancolombia.commonsvnt.api.validations.ValidationOptionalList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_NUMBERS_TAM_TWO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TaxInfoRequestDataCountryList {

    @JsonProperty("identifier")
    @ApiModelProperty(example = "1", value = "")
    @Pattern(regexp = REGEX_NUMBERS_TAM_TWO, groups = ValidationOptionalList.class)
    @Valid
    private String identifier;

    @JsonProperty("country")
    @ApiModelProperty(example = "PAISRE_RS1", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptionalList.class)
    @Valid
    private String country;

    @JsonProperty("taxId")
    @ApiModelProperty(example = "2452526", value = "")
    @Size(min = 1, max = 20, groups = ValidationOptionalList.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC, groups = ValidationOptionalList.class)
    private String taxId;
}