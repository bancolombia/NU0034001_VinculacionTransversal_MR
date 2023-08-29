package co.com.bancolombia.commonsvnt.api.model.util;

import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * UserInfoRequestData
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class UserInfoRequestData {

    @JsonProperty("acquisitionId")
    @ApiModelProperty(required = true, value = "")
    @Size(max = 40, groups = ValidationMandatory.class)
    @Pattern(regexp = ConstantsRegex.REGEX_UUID, groups = ValidationMandatory.class)
    @NotNull(groups = {ValidationAcquisitionId.class})
    protected String acquisitionId;

    @JsonProperty("documentType")
    @ApiModelProperty(required = true, value = "TIPDOC_FS001")
    @NotNull(groups = ValidationMandatory.class)
    @Size(min = 12, max = 12, groups = ValidationMandatory.class)
    protected String documentType;

    @JsonProperty("documentNumber")
    @ApiModelProperty(required = true, value = "1061000000")
    @NotNull(groups = ValidationMandatory.class)
    @Size(min = 1, max = 20, groups = ValidationMandatory.class)
    @Pattern(regexp = ConstantsRegex.REGEX_ALPHANUMERIC_WITHOUT_SPECIALS, groups = ValidationMandatory.class)
    protected String documentNumber;


    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "N\u00famero de documento inv\u00e1lido", groups = ValidationMandatory.class)
    private boolean isDocumentNumber() {
        if (this.documentNumber == null ||
                this.documentNumber.isEmpty() ||
                this.documentType == null ||
                this.documentType.isEmpty()) {
            return true;
        }
        return !(this.documentType.compareTo(Constants.DOCUMENT_TYPE_CC) == 0
                && !this.documentNumber
                .matches(ConstantsRegex.REGEX_NUMBERS_TAM_TWENTY));
    }
}
