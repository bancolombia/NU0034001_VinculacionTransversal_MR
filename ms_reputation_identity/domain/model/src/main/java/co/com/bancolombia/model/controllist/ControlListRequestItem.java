package co.com.bancolombia.model.controllist;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ControlListRequestItem {

    private String system;
    private String product;
    private String customerDocumentType;
    private String customerDocumentId;
    private String customerFullName;
}
