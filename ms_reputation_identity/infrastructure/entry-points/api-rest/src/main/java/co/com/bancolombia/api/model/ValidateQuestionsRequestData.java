package co.com.bancolombia.api.model;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * ValidateQuestionsRequestData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateQuestionsRequestData extends UserInfoRequestData {

    @ApiModelProperty(example = "01", value = "")
    @Size(min = 1, max = 10, groups = ValidationMandatory.class)
    @Pattern(regexp = ConstantsRegex.REGEX_NUMBERS_TAM_TWENTY, groups = ValidationMandatory.class)
    @JsonProperty("questionnaireId")
    @NotNull
    private String questionnaireId;

    @ApiModelProperty(example = "10", value = "")
    @Size(min = 1, max = 10, groups = ValidationMandatory.class)
    @Pattern(regexp = ConstantsRegex.REGEX_NUMBERS_TAM_TWENTY, groups = ValidationMandatory.class)
    @JsonProperty("questionnaireRecordId")
    @NotNull
    private String questionnaireRecordId;


    @JsonProperty("answersList")
    @ApiModelProperty(value = "")
    @Valid
    @NotNull
    private List<ValidateQuestionRequestDataAnswersList> answerList;
}
