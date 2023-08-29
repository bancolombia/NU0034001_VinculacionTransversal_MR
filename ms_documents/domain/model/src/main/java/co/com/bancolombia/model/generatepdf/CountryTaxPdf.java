package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryTaxPdf {
    private String country;
    private String idTax;
}
