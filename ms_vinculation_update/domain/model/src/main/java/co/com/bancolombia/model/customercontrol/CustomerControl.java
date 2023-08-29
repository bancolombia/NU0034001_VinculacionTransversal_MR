package co.com.bancolombia.model.customercontrol;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class CustomerControl extends Auditing {
    private UUID id;
    private String documentType;
    private String documentNumber;
    private String operation;
    private String state;
    private Date registrationDate;
    private Date unlockDate;
}