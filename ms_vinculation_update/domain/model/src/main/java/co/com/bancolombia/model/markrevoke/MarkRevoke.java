package co.com.bancolombia.model.markrevoke;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MarkRevoke extends Auditing {
    private UUID id;
    private String virtualCosts;
    private String virtualExtracts;
    private String electronicMediaExtracts;
    private String sharingInformation;
    private String telesalesCommercialOffers;
    private String sendTextMessage;
    private String sendingEmails;
    private Acquisition acquisition;
}
