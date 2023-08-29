package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInformationPdf {
    private String residenceAddress;
    private String neighborhood;
    private String city;
    private String department;
    private String country;
    private String phone;
    private String cellPhone;
    private String email;
}
