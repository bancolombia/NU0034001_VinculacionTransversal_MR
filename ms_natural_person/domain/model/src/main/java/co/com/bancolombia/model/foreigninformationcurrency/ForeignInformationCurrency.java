package co.com.bancolombia.model.foreigninformationcurrency;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ForeignInformationCurrency extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("FOREIGNCURRENCYTRANSACTIONTYPE")
    private String foreignCurrencyTransactionType;
    @ExecFieldAnnotation("WHICH")
    private String which;
    @ExecFieldAnnotation("NAMEENTITY")
    private String nameEntity;
    @ExecFieldAnnotation("PRODUCTTYPE")
    private String productType;
    @ExecFieldAnnotation("PRODUCTNUMBER")
    private String productNumber;
    @ExecFieldAnnotation("AVERAGEMONTHLYAMOUNT")
    private BigDecimal averageMonthlyAmount;
    @ExecFieldAnnotation("CURRENCY")
    private String currency;
    @ExecFieldAnnotation("DEPARTMENT")
    private String department;
    @ExecFieldAnnotation("CITY")
    private String city;
    @ExecFieldAnnotation("COUNTRY")
    private String country;
    private ForeignInformation foreignInformation;
    private Acquisition acquisition;
}