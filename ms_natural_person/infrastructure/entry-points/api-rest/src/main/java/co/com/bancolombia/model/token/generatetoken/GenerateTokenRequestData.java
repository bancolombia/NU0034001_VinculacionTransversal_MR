package co.com.bancolombia.model.token.generatetoken;

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
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_EMAIL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class GenerateTokenRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "3193472013", value = "")
    @Size(min = 10, max = 30, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_CELLPHONE, groups = ValidationOptional.class)
    @JsonProperty("cellphone")
    private String cellphone;

    @ApiModelProperty(example = "user@test.com", value = "")
    @Size(min = 1, max = 241, groups = ValidationOptional.class)
    @Pattern(regexp = REGEX_EMAIL, groups = ValidationOptional.class)
    @JsonProperty("email")
    private String email;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "Validate email: SININFORMACION', 'NOINFORMA', 'SINCORREO", groups = ValidationOptional.class)
    private boolean isEmail() {
        return ValidationJsonFields.validationEmail(this.email);
    }
}
