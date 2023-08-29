package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyInformationPdf {
    private String companyNames;
    private String companyAddress;
    private String companyNeighborhood;
    private String companyCity;
    private String companyDepartment;
    private String companyCountry;
    private String companyPhone;
    private String companyPhoneExt;
    private String companyCellPhone;
    private String companyEmail;
}
