package co.com.bancolombia.model.uploaddocument;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class DigitalizationRutSave extends Auditing {
    private UUID id;
    private String messageId;
    private Acquisition acquisition;
    private String taxIdentificationNumber;
    private String tinConfidence;
    private String corporateName;
    private String cnConfidence;
    private String firstName;
    private String fnConfidence;
    private String secondName;
    private String snConfidence;
    private String firstSurname;
    private String fsConfidence;
    private String secondSurname;
    private String ssConfidence;
    private String identificationType;
    private String itConfidence;
    private String mainActivity;
    private String maConfidence;
    private String secondaryActivity;
    private String saConfidence;
    private String emissionDate;
    private String edConfidence;
    private String assesseeType;
    private String atConfidence;
}
