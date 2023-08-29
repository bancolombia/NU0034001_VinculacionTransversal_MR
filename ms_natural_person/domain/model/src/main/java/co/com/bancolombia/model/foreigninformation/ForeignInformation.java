package co.com.bancolombia.model.foreigninformation;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ForeignInformation extends Auditing implements Mergeable {
    private UUID id;
    @ExecFieldAnnotation("FOREIGNCURRENCYTRANSACTION")
    private String foreignCurrencyTransaction;
    private Acquisition acquisition;

    @ExecFieldAnnotation("FOREIGNCURRENCYLIST")
    private List<ForeignInformationCurrency> foreignCurrencyList;
}