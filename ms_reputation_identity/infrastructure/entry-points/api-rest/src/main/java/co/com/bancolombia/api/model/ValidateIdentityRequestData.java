package co.com.bancolombia.api.model;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationOptional;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * ValidateIdentityRequestData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateIdentityRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "Perez", value = "")
    @Size(min = 2, max = 40, groups = ValidationOptional.class)
    @Pattern(regexp = ConstantsRegex.REGEX_LETTERS, groups = ValidationOptional.class)
    @JsonProperty("firstSurname")
    private String firstSurname;
}
