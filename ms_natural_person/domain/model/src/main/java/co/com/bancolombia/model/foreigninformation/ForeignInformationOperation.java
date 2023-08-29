package co.com.bancolombia.model.foreigninformation;

import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ForeignInformationOperation {
    private ForeignInformation foreignInformation;
    private List<ForeignInformationCurrency> list;
}