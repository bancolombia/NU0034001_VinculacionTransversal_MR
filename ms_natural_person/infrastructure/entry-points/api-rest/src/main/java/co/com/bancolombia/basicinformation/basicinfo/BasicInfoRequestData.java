package co.com.bancolombia.basicinformation.basicinfo;

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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DATE_FORMAT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_NUMBERS_TAM_TWO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class BasicInfoRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "GENERO_M", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @JsonProperty("gender")
    private String gender;

    @ApiModelProperty(example = "CIUDAD_05001000", value = "")
    @Size(min = 1, max = 15, groups = ValidationOptional.class)
    @JsonProperty("birthCity")
    private String birthCity;

    @ApiModelProperty(example = "PAIS_CO", value = "")
    @Size(min = 1, max = 7, groups = ValidationOptional.class)
    @JsonProperty("country")
    private String country;

    @ApiModelProperty(example = "DPTO_CO_000005", value = "")
    @Size(min = 1, max = 14, groups = ValidationOptional.class)
    @JsonProperty("birthDepartment")
    private String birthDepartment;

    @ApiModelProperty(example = "ESTCIV_2", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @JsonProperty("civilStatus")
    private String civilStatus;

    @ApiModelProperty(example = "NACION_CO", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("nationality")
    private String nationality;

    @ApiModelProperty(example = "22", value = "")
    @Pattern(regexp = REGEX_NUMBERS_TAM_TWO, groups = ValidationOptional.class)
    @JsonProperty("dependants")
    private String dependants;

    @ApiModelProperty(example = "NIVACA_02", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("educationLevel")
    private String educationLevel;

    @ApiModelProperty(example = "ESTRAT_01", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("socialStratum")
    private String socialStratum;

    @ApiModelProperty(example = "TIPVIV_02", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("housingType")
    private String housingType;

    @ApiModelProperty(example = "RESPUE_N", value = "")
    @Size(min = 1, max = 8, groups = ValidationOptional.class)
    @JsonProperty("pep")
    private String pep;

    @ApiModelProperty(example = "TIPCON_01", value = "")
    @Size(min = 1, max = 9, groups = ValidationOptional.class)
    @JsonProperty("contractType")
    private String contractType;

    @ApiModelProperty(example = "2020-07-12", value = "")
    @Pattern(regexp = REGEX_DATE_FORMAT, groups = ValidationOptional.class)
    @Size(min = 10, max = 10, groups = ValidationOptional.class)
    @JsonProperty("entryCompanyDate")
    private String entryCompanyDate;
}