package co.com.bancolombia.model.markcustomer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Meta {

    @JsonProperty("_messageId")
    private String messageId;

    @JsonProperty("_requestDate")
    private Date requestDate;

    @JsonProperty("_version")
    private String version;

    @JsonProperty("_clientRequest")
    private String clientRequest;
}
