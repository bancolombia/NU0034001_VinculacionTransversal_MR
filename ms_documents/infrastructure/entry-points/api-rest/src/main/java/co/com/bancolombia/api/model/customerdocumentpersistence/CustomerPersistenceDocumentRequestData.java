package co.com.bancolombia.api.model.customerdocumentpersistence;

import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_PERSISTENCE_DOCUMENT_CODE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class CustomerPersistenceDocumentRequestData extends UserInfoRequestData {
    @ApiModelProperty(example = "001", value = "")
    @Size(min = 3, max = 3, groups = ValidationMandatory.class)
    @NotNull(groups = ValidationMandatory.class)
    @Pattern(regexp = REGEX_PERSISTENCE_DOCUMENT_CODE, groups = ValidationMandatory.class)
    @JsonProperty("documentCode")
    @Valid
    private String documentCode;
}
