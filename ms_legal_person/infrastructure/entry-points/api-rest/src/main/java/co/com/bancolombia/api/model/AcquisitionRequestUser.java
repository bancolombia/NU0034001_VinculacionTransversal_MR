package co.com.bancolombia.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * AcquisitionRequestUser
 */
@Validated
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionRequestUser {

    @JsonProperty("documentType")
    @ApiModelProperty(required = true, value = "", example = "TIPDOC_FS001 ")
    @NotNull
    @Valid
    @Size(min = 12, max = 12)
    private String documentType;

    @JsonProperty("documentNumber")
    @ApiModelProperty(required = true, value = "1061000000", example = "1061000000")
    @NotNull
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String documentNumber;

    @JsonProperty("productId")
    @ApiModelProperty(example = "001", required = true, value = "")
    @NotNull
    @Size(min = 1, max = 9)
    private String productId;

    @JsonProperty("channelId")
    @ApiModelProperty(example = "001", required = true, value = "")
    @NotNull
    @Size(min = 1, max = 9)
    private String channelId;

    @JsonProperty("businessLineId")
    @ApiModelProperty(example = "001", required = true, value = "")
    @NotNull
    @Size(min = 1, max = 3)
    private String businessLineId;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "N\u00famero de documento inv\u00e1lido")
    private boolean isDocumentNumber() {
        if (this.documentNumber == null ||
                this.documentNumber.isEmpty() ||
                this.documentType == null ||
                this.documentType.isEmpty()) {
            return true;
        }
        return !(this.documentType.compareTo("TIPDOC_FS001") == 0
                && !this.documentNumber
                .matches("^\\d{1,20}$"));
    }
}
