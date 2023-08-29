package co.com.bancolombia.api.model.uploaddocument;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class UploadDocumentFilesListResponse {

    @JsonProperty("documentalTypeCode")
    @ApiModelProperty(example = "01", value = "")
    private String documentalTypeCode;

    @JsonProperty("documentalSubTypeCode")
    @ApiModelProperty(example = "001", value = "")
    private String documentalSubTypeCode;

    @JsonProperty("fileName")
    @ApiModelProperty(example = "01_TIPDOC_FS001_1017181715.pdf", value = "")
    private String fileName;

    @JsonProperty("outComeCode")
    @ApiModelProperty(example = "00", value = "")
    private String outComeCode;

    @JsonProperty("outComeName")
    @ApiModelProperty(example = "EXITO", value = "")
    private String outComeName;

    @JsonProperty("reason")
    @ApiModelProperty(example = "Se ha procesado exitosamente el documento", value = "")
    private String reason;
}
