package co.com.bancolombia.model.signdocument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class SDResponseMeta {

    @JsonProperty("_messageId")
    private String _messageId;

    @JsonProperty("_requestDateTime")
    private Date _requestDateTime;

    @JsonProperty("_applicationId")
    private String _applicationId;
}
