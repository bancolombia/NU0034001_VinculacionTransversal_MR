package co.com.bancolombia.model.expoquestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireSave {

    private String questionnarieId;
    private String questionnaireRecordId;
    private String questionnarieResult;
    private String numberAttemptsYear;
    private String numberAttemptsMonth;
    private String numberAttemptsDay;
    private String excludeCustomerParameterized;
    private String messageId;
    private Date requestDate;
    private Date createdDate;
    private Date updatedDate;
    private String updatedBy;
    private QuestionnaireAlert alert;
    private List<QuestionnaireQuestion> question;
    private List<QuestionnaireResponseOption> responseOptions;
}
