package co.com.bancolombia.model.markcustomer;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class RegisterControlListSave {

    private String id;
    private String acquisitionId;
    private String messageId;
    private Date requestDate;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
}
