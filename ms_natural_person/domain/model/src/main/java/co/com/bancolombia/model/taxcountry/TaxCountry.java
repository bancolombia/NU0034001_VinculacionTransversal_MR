package co.com.bancolombia.model.taxcountry;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.taxinformation.TaxInformation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TaxCountry extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("IDENTIFIERTC")
    private Integer identifier;
    @ExecFieldAnnotation("TAXIDTC")
    private String taxId;
    @ExecFieldAnnotation("COUNTRYTC")
    private String country;
    private TaxInformation taxInformation;
    private Acquisition acquisition;
}