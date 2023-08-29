package co.com.bancolombia.foreigninformation.model;

import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.usecase.util.ValidationJsonFields;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DECIMAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_NAME_ENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_PRODUCT_NUMBER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ForeignCurrencyInfoRequestDataList {

    @ApiModelProperty(example = "TIPOPE_004", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @JsonProperty("foreignCurrencyTransactionType")
    private String foreignCurrencyTransactionType;

    @ApiModelProperty(example = "Example", value = "")
    @Size(min = 1, max = 20, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC, groups = ValidationOptional.class)
    @JsonProperty("which")
    private String which;

    @ApiModelProperty(example = "Example", value = "")
    @Size(min = 1, max = 60, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_NAME_ENTITY, groups = ValidationOptional.class)
    @JsonProperty("nameEntity")
    private String nameEntity;

    @ApiModelProperty(example = "TIPOPR_02", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("productType")
    private String productType;

    @ApiModelProperty(example = "2212-J", value = "")
    @Size(min = 1, max = 18, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_PRODUCT_NUMBER, groups = ValidationOptional.class)
    @JsonProperty("productNumber")
    private String productNumber;

    @ApiModelProperty(example = "12.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("averageMonthlyAmount")
    private String averageMonthlyAmount;

    @ApiModelProperty(example = " MONEDA_COP", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @JsonProperty("currency")
    private String currency;

    @JsonProperty("country")
    @ApiModelProperty(example = "PAIS_CO", value = "")
    @Size(min = 1, max = 7, groups = ValidationOptional.class)
    private String country;

    @JsonProperty("department")
    @ApiModelProperty(example = "DPTO_CO_000005", value = "")
    @Size(min = 1, max = 14, groups = ValidationOptional.class)
    private String department;

    @JsonProperty("city")
    @ApiModelProperty(example = "CIUDAD_05001000", value = "")
    @Size(min = 1, max = 15, groups = ValidationOptional.class)
    private String city;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "validate nameEntity injection HTML", groups = ValidationOptional.class)
    private boolean isNameEntity() {
        return ValidationJsonFields.validationInjectionHTML(this.nameEntity);
    }
}