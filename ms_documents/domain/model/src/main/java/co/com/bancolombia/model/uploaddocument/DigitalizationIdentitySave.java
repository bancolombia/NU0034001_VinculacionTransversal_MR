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
public class DigitalizationIdentitySave extends Auditing {
    private UUID id;
    private String messageId;
    private Acquisition acquisition;
    private String docNumberOCR;
    private String dnConfidenceOCR;
    private String firstNamesOCR;
    private String fnConfidenceOCR;
    private String lastNamesOCR;
    private String lnConfidenceOCR;
    private String birthDateOCR;
    private String bdConfidenceOCR;
    private String birthPlaceOCR;
    private String bpConfidenceOCR;
    private String emissionDateOCR;
    private String edConfidenceOCR;
    private String emissionPlaceOCR;
    private String epConfidenceOCR;
    private String genderOCR;
    private String gdConfidenceOCR;
    private String docNumberBCR;
    private String dnConfidenceBCR;
    private String firstNamesBCR;
    private String fnConfidenceBCR;
    private String lastNamesBCR;
    private String lnConfidenceBCR;
    private String genderBCR;
    private String gdConfidenceBCR;
    private String birthDateBCR;
    private String bdConfidenceBCR;
    private String coincidenceDocNumber;

}
