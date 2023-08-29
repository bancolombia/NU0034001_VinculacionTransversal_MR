package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForeignCurrencyPdf {
    private String entityName;
    private String productType;
    private String productNumber;
    private String averageMonthlyAmount;
    private String currency;
    private String city;
    private String country;
    private String foreignCurrencyTransactionType;
}
