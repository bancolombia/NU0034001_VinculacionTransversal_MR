package co.com.bancolombia.model.asyncdigitalization;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class AsyncDigitalization extends Auditing {
    private UUID id;
    private String dataResponse;
    private String errorResponse;
    private String requestReuse;
    private Date requestDateReuse;
    private String responseReuse;
    private Date responseDateReuse;
    private Acquisition acquisition;
    private SqsMessage sqsMessage;
}
