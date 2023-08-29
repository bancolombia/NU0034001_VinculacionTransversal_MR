package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EconomicInformationPdf {
    private String profession;
    private String job;
    private String economicActivity;
    private String codeCiiu;
    private String numberEmployees;
}
