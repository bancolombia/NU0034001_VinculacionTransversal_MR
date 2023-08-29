package co.com.bancolombia.api.model;

import co.com.bancolombia.commonsvnt.api.validations.ValidationInvalidInputList;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatoryInputList;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_NUMBERS_TAM_TWENTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateQuestionRequestDataAnswersList {

    @JsonProperty("questionId")
    @ApiModelProperty(example = "3", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 3, groups = ValidationInvalidInputList.class)
    @Pattern(regexp = REGEX_NUMBERS_TAM_TWENTY
            , groups = ValidationInvalidInputList.class)
    private String questionId;

    @JsonProperty("answerId")
    @ApiModelProperty(example = "3", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 3, groups = ValidationInvalidInputList.class)
    @Pattern(regexp = REGEX_NUMBERS_TAM_TWENTY
            , groups = ValidationInvalidInputList.class)
    private String answerId;
}
