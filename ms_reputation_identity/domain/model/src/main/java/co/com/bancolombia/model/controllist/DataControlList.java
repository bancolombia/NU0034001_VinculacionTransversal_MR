package co.com.bancolombia.model.controllist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DataControlList {

    private HeaderControlList header;
    private StatusControlList status;
    private String system;
    private String product;
    private String customerDocumentType;
    private String customerDocumentId;
    private String customerFullName;
    private String customerType;
    private String thirdPartyControl;
    private String customerStatus;
    private String state;
    private String alerts;
    private String message;
    private String categories;
    private String ofacMessageOther;
    private String passport;
}
