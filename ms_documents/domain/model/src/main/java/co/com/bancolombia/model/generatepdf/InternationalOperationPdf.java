package co.com.bancolombia.model.generatepdf;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InternationalOperationPdf {
    private String performsForeignCurrencyOperations;
    private List<String> foreignCurrencyTransactionType;
    private List<String> whichForeignCurrencyOperation;
    private List<ForeignCurrencyPdf> foreignCurrencyList;
}
