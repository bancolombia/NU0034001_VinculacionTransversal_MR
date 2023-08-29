package co.com.bancolombia.model.controllist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MetaControlList {

    @JsonProperty("_messageId")
    private String messageId;

    @JsonProperty("_version")
    private String version;

    @JsonProperty("_requestDate")
    private String requestDate;

    @JsonProperty("_responseSize")
    private String responseSize;

    @JsonProperty("_clientRequest")
    private String clientRequest;
}
