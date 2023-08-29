package co.com.bancolombia.model.validatedataextraction;


import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Builder
@NoArgsConstructor
@Validated
@AllArgsConstructor
public class UploadDocumentApiResponse {
    @JsonProperty("meta")
    @Valid
    private MetaResponse meta;

    @JsonProperty("data")
    @Valid
    private UploadDocumentApiResponseData data;
}
