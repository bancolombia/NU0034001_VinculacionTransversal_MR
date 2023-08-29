package co.com.bancolombia.personalinformation.model;

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

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DATE_FORMAT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_LETTERS_SPACES;

/**
 * PersonalInfoRequestData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class PersonalInfoRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "Pedro", value = "")
    @Size(min = 2, max = 40, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_LETTERS_SPACES, groups = ValidationOptional.class)
    @JsonProperty("firstName")
    private String firstName;

    @ApiModelProperty(example = "Pablo", value = "")
    @Size(min = 2, max = 40, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_LETTERS_SPACES, groups = ValidationOptional.class)
    @JsonProperty("secondName")
    private String secondName;

    @ApiModelProperty(example = "Perez", value = "")
    @Size(min = 2, max = 40, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_LETTERS_SPACES, groups = ValidationOptional.class)
    @JsonProperty("firstSurname")
    private String firstSurname;

    @ApiModelProperty(example = "Diaz", value = "")
    @Size(min = 2, max = 40, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_LETTERS_SPACES, groups = ValidationOptional.class)
    @JsonProperty("secondSurname")
    private String secondSurname;

    @ApiModelProperty(example = "user@test.com", value = "")
    @Size(min = 1, max = 241, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_EMAIL, groups = ValidationOptional.class)
    @JsonProperty("email")
    private String email;

    @ApiModelProperty(example = "2020-06-12", value = "")
    @Pattern(regexp = REGEX_DATE_FORMAT
            , groups = ValidationOptional.class)
    @Size(min = 10, max = 10, groups = ValidationOptional.class)
    @JsonProperty("expeditionDate")
    private String expeditionDate;

    @ApiModelProperty(example = "CIUDAD_05001000", value = "")
    @Size(min = 1, max = 15, groups = ValidationOptional.class)
    @JsonProperty("expeditionPlace")
    private String expeditionPlace;

    @ApiModelProperty(example = "PAIS_CO", value = "")
    @Size(min = 1, max = 7, groups = ValidationOptional.class)
    @JsonProperty("expeditionCountry")
    private String expeditionCountry;

    @ApiModelProperty(example = "DPTO_CO_000005", value = "")
    @Size(min = 1, max = 14, groups = ValidationOptional.class)
    @JsonProperty("expeditionDepartment")
    private String expeditionDepartment;

    @ApiModelProperty(example = "1995-12-18", value = "")
    @Pattern(regexp = REGEX_DATE_FORMAT
            , groups = ValidationOptional.class)
    @Size(min = 10, max = 10, groups = ValidationOptional.class)
    @JsonProperty("birthdate")
    private String birthdate;

    @ApiModelProperty(example = "3001239873", value = "")
    @Size(min = 10, max = 30, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_CELLPHONE
            , groups = ValidationOptional.class)
    @JsonProperty("cellphone")
    private String cellphone;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "Validación correo electrónico: SININFORMACION', 'NOINFORMA', 'SINCORREO"
            , groups = ValidationOptional.class)
    private boolean isEmail() {
        return ValidationJsonFields.validationEmail(this.email);
    }
}
