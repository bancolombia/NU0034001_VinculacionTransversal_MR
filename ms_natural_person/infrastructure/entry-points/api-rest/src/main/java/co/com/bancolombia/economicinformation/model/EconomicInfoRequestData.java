package co.com.bancolombia.economicinformation.model;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DATE_FORMAT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DECIMAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_EMPLOYEES_NUMBER;

/**
 * EconomicInfoRequestData
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class EconomicInfoRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "PROFES_Z001", value = "")
    @Size(min = 1, max = 11, groups = ValidationOptional.class)
    @JsonProperty("profession")
    private String profession;

    @ApiModelProperty(example = "CARGO_Z001", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @JsonProperty("positionTrade")
    private String positionTrade;

    @ApiModelProperty(example = "OCUPAC_01", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("occupation")
    private String occupation;

    @ApiModelProperty(example = "libre texto", value = "")
    @Size(min = 1, max = 60, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC, groups = ValidationOptional.class)
    @JsonProperty("economicActivity")
    private String economicActivity;

    @ApiModelProperty(example = "CIIU_00000", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @JsonProperty("ciiu")
    private String ciiu;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("monthlyIncome")
    private String monthlyIncome;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("otherMonthlyIncome")
    private String otherMonthlyIncome;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("totalAssets")
    private String totalAssets;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("totalLiabilities")
    private String totalLiabilities;

    @ApiModelProperty(example = " MONEDA_COP", value = "")
    @Size(min = 1, max = 10, groups = ValidationOptional.class)
    @JsonProperty("currency")
    private String currency;

    @ApiModelProperty(example = "Detalle", value = "")
    @Size(min = 1, max = 30, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC, groups = ValidationOptional.class)
    @JsonProperty("detailOtherMonthlyIncome")
    private String detailOtherMonthlyIncome;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("totalMonthlyExpenses")
    private String totalMonthlyExpenses;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("annualSales")
    private String annualSales;

    @ApiModelProperty(example = "2020-12-25", value = "")
    @Pattern(regexp = REGEX_DATE_FORMAT
            , groups = ValidationOptional.class)
    @Size(min = 10, max = 10, groups = ValidationOptional.class)
    @JsonProperty("closingDateSales")
    private String closingDateSales;

    @ApiModelProperty(example = "1240.12", value = "")
    @Pattern(regexp = REGEX_DECIMAL, groups = ValidationOptional.class)
    @JsonProperty("patrimony")
    private String patrimony;

    @ApiModelProperty(example = " 150", value = "")
    @Size(min = 1, max = 7, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_EMPLOYEES_NUMBER, groups = ValidationOptional.class)
    @JsonProperty("employeesNumber")
    private String employeesNumber;

    @ApiModelProperty(example = " RESPUE_N", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @JsonProperty("rut")
    private String rut;
}

