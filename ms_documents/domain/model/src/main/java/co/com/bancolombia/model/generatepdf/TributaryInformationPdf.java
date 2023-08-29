package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TributaryInformationPdf {
    private String incomeDeclarant;
    private String withholdingAgent;
    private String regimeIva;
    private String declareTaxInAnotherCountry;
    private List<CountryTaxPdf> countryTax;
    private String originAssetComeFrom;
    private String originAssetComeFromCountry;
    private String originAssetComeFromCity;
}
