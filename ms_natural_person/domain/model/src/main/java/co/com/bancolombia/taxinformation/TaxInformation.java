package co.com.bancolombia.taxinformation;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TaxInformation extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("DECLARINGINCOME")
    private String declaringIncome;
    @ExecFieldAnnotation("WITHHOLDINGAGENT")
    private String withHoldingAgent;
    @ExecFieldAnnotation("VATREGIME")
    private String vatRegime;
    @ExecFieldAnnotation("ORIGINASSETCOMEFROM")
    private String originAssetComeFrom;
    @ExecFieldAnnotation("SOURCECOUNTRYRESOURCE")
    private String sourceCountryResource;
    @ExecFieldAnnotation("SOURCECITYRESOURCE")
    private String sourceCityResource;
    @ExecFieldAnnotation("REQUIREDTOTAXUSTAX")
    private String requiredToTaxUsTax;
    @ExecFieldAnnotation("TAXID")
    private String taxId;
    @ExecFieldAnnotation("COUNTRY")
    private String country;
    @ExecFieldAnnotation("BUSINESSTAXPAYMENT")
    private String businessTaxPayment;
    @ExecFieldAnnotation("SOCIALSECURITYPAYMENT")
    private String socialSecurityPayment;
    @ExecFieldAnnotation("DECLARETAXINANOTHERCOUNTRY")
    private String declareTaxInAnotherCountry;
    private Acquisition acquisition;
    
    @ExecFieldAnnotation("COUNTRYLIST")
    private List<TaxCountry> countryList;
}