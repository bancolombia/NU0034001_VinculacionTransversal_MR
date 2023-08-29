package co.com.bancolombia.taxinformation.model;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.usecase.util.ValidationJsonFields;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_COMPANY_NAME;

/**
 * TaxInfoRequestData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TaxInfoRequestData extends UserInfoRequestData {

    @JsonProperty("declaringIncome")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @Valid
    private String declaringIncome;

    @JsonProperty("withHoldingAgent")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @Valid
    private String withHoldingAgent;

    @JsonProperty("vatRegime")
    @ApiModelProperty(example = "REGIME_02", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @Valid
    private String vatRegime;

    @JsonProperty("originAssetComeFrom")
    @ApiModelProperty(example = "libre texto", value = "")
    @Size(min = 1, max = 60, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_COMPANY_NAME, groups = ValidationOptional.class)
    private String originAssetComeFrom;

    @JsonProperty("sourceCountryResource")
    @ApiModelProperty(example = "PAIS_CO", value = "")
    @Size(min = 1, max = 7, groups = ValidationOptional.class)
    @Valid
    private String sourceCountryResource;

    @JsonProperty("sourceCityResource")
    @ApiModelProperty(example = "CIUDAD_05001000", value = "")
    @Size(min = 1, max = 15, groups = ValidationOptional.class)
    @Valid
    private String sourceCityResource;

    @JsonProperty("requiredToTaxUsTax")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @Valid
    private String requiredToTaxUsTax;

    @JsonProperty("taxId")
    @ApiModelProperty(example = "123456", value = "")
    @Size(min = 1, max = 20, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC, groups = ValidationOptional.class)
    private String taxId;

    @JsonProperty("country")
    @ApiModelProperty(example = "PAISRE_RS1", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @Valid
    private String country;

    @JsonProperty("businessTaxPayment")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @Valid
    private String businessTaxPayment;

    @JsonProperty("socialSecurityPayment")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @Valid
    private String socialSecurityPayment;

    @JsonProperty("declareTaxInAnotherCountry")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @Valid
    private String declareTaxInAnotherCountry;

    @JsonProperty("countryList")
    @ApiModelProperty(value = "")
    @Valid
    private List<TaxInfoRequestDataCountryList> taxInfoRequestDataCountryList;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "validate CompanyName injection HTML"
            , groups = ValidationOptional.class)
    private boolean isOriginAssetComeFrom() {
        return ValidationJsonFields.validationInjectionHTML(this.originAssetComeFrom);
    }
}