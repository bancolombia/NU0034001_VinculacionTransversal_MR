package co.com.bancolombia.api.model.uploaddocument;

import co.com.bancolombia.commonsvnt.api.validations.ValidationFileExtencion;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_ALPHANUMERIC_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DOCUMENT_SUBTYPE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_DOCUMENT_TYPE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_NUMBERS_TAM_TWENTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class UploadDocumentFilesListRequest {

    @JsonProperty("documentalTypeCode")
    @ApiModelProperty(example = "01", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 2, groups = ValidationInvalidInputList.class)
    @Pattern(regexp = REGEX_DOCUMENT_TYPE_DIGITALIZATION, groups = ValidationMandatoryInputList.class)
    @Pattern(regexp = REGEX_NUMBERS_TAM_TWENTY, groups = ValidationInvalidInputList.class)
    private String documentalTypeCode;

    @JsonProperty("documentalSubTypeCode")
    @ApiModelProperty(example = "001", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 3, max = 3, groups = ValidationInvalidInputList.class)
    @Pattern(regexp = REGEX_DOCUMENT_SUBTYPE_DIGITALIZATION, groups = ValidationInvalidInputList.class)
    private String documentalSubTypeCode;

    @JsonProperty("fileName")
    @ApiModelProperty(example = "001_TIPDOC_FS001_1017181715.pdf", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 80, groups = ValidationInvalidInputList.class)
    @Pattern(regexp = REGEX_ALPHANUMERIC_UPLOAD_DOCUMENT, groups = ValidationFileExtencion.class)
    private String fileName;

    @JsonProperty("flagDataExtraction")
    @ApiModelProperty(example = "RESPUE_S", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 8, groups = ValidationInvalidInputList.class)
    private String flagDataExtraction;

    @JsonProperty("flagSynchronous")
    @ApiModelProperty(example = "RESPUE_N", value = "")
    @NotNull(groups = ValidationMandatoryInputList.class)
    @Size(min = 1, max = 8, groups = ValidationInvalidInputList.class)
    private String flagSynchronous;
}
