package co.com.bancolombia.model.economicinformation;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class EconomicInformation extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("PROFESSION")
    private String profession;
    @ExecFieldAnnotation("POSITIONTRADE")
    private String positionTrade;
    @ExecFieldAnnotation("OCUPPATION")
    private String occupation;
    @ExecFieldAnnotation("ECONOMICACTIVITY")
    private String economicActivity;
    private String economicActivityMark;
    @ExecFieldAnnotation("CIUU")
    private String ciiu;
    private String flagCiiu;
    @ExecFieldAnnotation("MONTHLINCOME")
    private BigDecimal monthlyIncome;
    @ExecFieldAnnotation("OTHERMONTHLINCOME")
    private BigDecimal otherMonthlyIncome;
    @ExecFieldAnnotation("TOTALASSETS")
    private BigDecimal totalAssets;
    @ExecFieldAnnotation("TOTALLABILITIES")
    private BigDecimal totalLiabilities;
    @ExecFieldAnnotation("CURRENCY")
    private String currency;
    @ExecFieldAnnotation("DETAILOTHERMONTHLYINCOME")
    private String detailOtherMonthlyIncome;
    @ExecFieldAnnotation("TOTALMONTHLYEXPENSES")
    private BigDecimal totalMonthlyExpenses;
    @ExecFieldAnnotation("ANNUALSALES")
    private BigDecimal annualSales;
    @ExecFieldAnnotation("CLOSINGDATESALES")
    private Date closingDateSales;
    @ExecFieldAnnotation("PATRIMONY")
    private BigDecimal patrimony;
    @ExecFieldAnnotation("EMPLOYESSNUMBER")
    private String employeesNumber;
    @ExecFieldAnnotation("RUT")
    private String rut;
    @ExecFieldAnnotation("REQUIREDRUT")
    private String requiredRut;
    private Acquisition acquisition;
}