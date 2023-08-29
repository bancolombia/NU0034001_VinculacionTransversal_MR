package co.com.bancolombia.api.model.customerdocumentpersistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Validated
@AllArgsConstructor
@Builder
@Data
public class PersistenceDocumentResponseData {
    @Valid
    @Size(min=1,max=2)
    @JsonProperty("answerCode")
    private String answerCode;

    @Valid
    @Size(min=10,max=60)
    @JsonProperty("answerName")
    private String answerName;
}
