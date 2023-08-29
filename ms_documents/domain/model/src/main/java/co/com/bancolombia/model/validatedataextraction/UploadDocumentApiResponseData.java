package co.com.bancolombia.model.validatedataextraction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@AllArgsConstructor
@Builder
@Data
public class UploadDocumentApiResponseData {
    @JsonProperty("processedDocuments")
    @Valid
    @NotNull
    private List<UploadDocumentFilesListResponse> processedDocuments;
}
