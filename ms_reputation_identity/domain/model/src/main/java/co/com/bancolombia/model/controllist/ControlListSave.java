package co.com.bancolombia.model.controllist;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ControlListSave {

    private String id;
    private String idAcquisition;
    private String customerDocumentType;
    private String customerDocumentId;
    private String thirdPartyControl;
    private String customerStatus;
    private String state;
    private String passport;
    private String alerts;
    private String message;
    private String categories;
    private String ofacMessageOther;
    private Date requestDate;
    private Date responseDate;
    private String messageId;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updateDate;
}
