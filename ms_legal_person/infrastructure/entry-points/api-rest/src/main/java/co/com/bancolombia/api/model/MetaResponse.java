package co.com.bancolombia.api.model;

import co.com.bancolombia.api.model.util.Meta;
import co.com.bancolombia.api.model.util.MetaRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class MetaResponse extends Meta {
    public MetaResponse() {

    }

    public MetaResponse(String systemId, String messageId, String version,
                        String requestDate, String service, String operation) {
        super(systemId, messageId, version, requestDate, service, operation);
    }

    public static MetaResponse fromMeta(MetaRequest meta) {
        MetaResponse ret = new MetaResponse();
        if (meta != null) {
            ret.systemId = meta.getSystemId();
            ret.messageId = meta.getMessageId();
            ret.version = meta.getVersion();
            ret.requestDate = meta.getRequestDate();
            ret.service = meta.getService();
            ret.operation = meta.getOperation();
        }
        return ret;
    }

}
