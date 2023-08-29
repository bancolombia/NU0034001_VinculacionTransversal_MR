package co.com.bancolombia.model.digitalizationprocesseddocuments;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class DigitalizationProcessedDocuments extends Auditing {
    private UUID id;
    private String documentalTypeCode;
    private String documentalSubTypeCode;
    private List<String> filesNames;
    private String flagDataExtraction;
    private String flagSynchronous;    
    private Date processingDate;
    private Acquisition acquisition;
}
