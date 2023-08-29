package co.com.bancolombia.model.token.validatetoken;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC_WITHOUT_SPECIALS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateTokenRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "229981", value = "")
    @Size(min = 1, max = 16, groups = ValidationMandatory.class)
    @NotNull(groups = ValidationMandatory.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC_WITHOUT_SPECIALS, groups = ValidationMandatory.class)
    @JsonProperty("tokenCode")
    private String tokenCode;
}
