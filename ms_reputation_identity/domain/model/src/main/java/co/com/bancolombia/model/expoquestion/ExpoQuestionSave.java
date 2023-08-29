package co.com.bancolombia.model.expoquestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ExpoQuestionSave {

    private String id;
    private String idAcquisition;
    private ValidateIdentificationSave validateIdentification;
    private QuestionnaireSave questionnaire;
    private String createdBy;
}
