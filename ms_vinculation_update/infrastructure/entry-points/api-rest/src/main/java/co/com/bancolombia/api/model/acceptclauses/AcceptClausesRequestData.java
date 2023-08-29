package co.com.bancolombia.api.model.acceptclauses;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ONE_OR_TWO;

/**
 * AcceptClausesRequestData
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class AcceptClausesRequestData extends UserInfoRequestData {

    @JsonProperty("acceptClauses")
    @ApiModelProperty(example = "1", required = true, value = "")
    @NotNull
    @Size(max = 1)
    @Pattern(regexp = REGEX_ONE_OR_TWO)
    @Valid
    private String acceptClauses;

    @JsonProperty("clauseCode")
    @ApiModelProperty(example = "CLAUSE001", required = true, value = "")
    @NotNull
    @Size(max = 10)
    @Pattern(regexp = REGEX_ALPHANUMERIC)
    @Valid
    private String clauseCode;

}
