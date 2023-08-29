package co.com.bancolombia.model.commons;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class BasicAcquisitionRequest {
    private String userTransaction;
    private String messageId;
}
