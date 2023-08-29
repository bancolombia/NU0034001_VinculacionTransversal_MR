package co.com.bancolombia.api.model.generatexposedocuments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@AllArgsConstructor
@Builder
@Data
public class GenExpDocsResponseData {
    @Valid
    @JsonProperty("url")
    private String url;
}
