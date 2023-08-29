package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FinancialInformationPdf {
    private String monthlyIncome;
    private String totalAssets;
    private String monthlyOtherIncome;
    private String totalPassives;
    private String detailOtherMonthIncome;
    private String totalMonthExpenses;
    private String annualSales;
    private Date closingSalesDate;
}
