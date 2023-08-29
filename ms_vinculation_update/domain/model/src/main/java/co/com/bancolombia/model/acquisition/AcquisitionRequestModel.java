package co.com.bancolombia.model.acquisition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AcquisitionRequestModel {
    private String documentType;
    private String documentNumber;
    private String typePerson;
    private String typeProduct;
    private String typeChannel;
    private String businessLine;
    private String token;
}
