package co.com.bancolombia.model.validatedataextraction;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String documentalTypeCode;

    @JsonProperty("documentalSubTypeCode")
    private String documentalSubTypeCode;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("outComeCode")
    private String outComeCode;

    @JsonProperty("outComeName")
    private String outComeName;

    @JsonProperty("reason")
    private String reason;


}
