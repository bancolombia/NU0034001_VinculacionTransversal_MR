package co.com.bancolombia.api.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class MetaRequest extends Meta{
    @JsonProperty("_usrMod")
    @ApiModelProperty(example = "BIZAGI")
    @Size(min = 1, max = 12)
    @NotNull
    private String usrMod;

    @JsonProperty("_ip")
    @ApiModelProperty(example = "ip")
    @Size(min = 1, max = 40)
    @NotNull
    private String ip;

    public MetaRequest() {

    }

    public MetaRequest(String systemId, String messageId, String version, String requestDate, String usrMod,
                       String ip, String service, String operation) {
        super(systemId, messageId, version, requestDate, service, operation);
        this.usrMod = usrMod;
        this.ip = ip;
    }
}
