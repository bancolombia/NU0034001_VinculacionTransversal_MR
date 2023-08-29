package co.com.bancolombia.api.model.validatedataextraction;

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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DOCUMENT_SUBTYPE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DOCUMENT_TYPE_DIGITALIZATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateDataExtractionRequestData extends UserInfoRequestData {
    @JsonProperty("documentalTypeCode")
    @ApiModelProperty(example = "01", value = "")
    @Pattern(regexp = REGEX_DOCUMENT_TYPE_DIGITALIZATION
            , groups = ValidationMandatory.class)
    @NotNull(groups = ValidationMandatory.class)
    @Size(min = 1, max = 2, groups = ValidationMandatory.class)
    private String documentalTypeCode;


    @JsonProperty("documentalSubTypeCode")
    @ApiModelProperty(example = "001", value = "")
    @NotNull(groups = ValidationMandatory.class)
    @Size(min = 3, max = 3, groups = ValidationMandatory.class)
    @Pattern(regexp = REGEX_DOCUMENT_SUBTYPE_DIGITALIZATION
            , groups = ValidationMandatory.class)
    private String documentalSubTypeCode;
}
