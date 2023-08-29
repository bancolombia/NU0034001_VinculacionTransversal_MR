package co.com.bancolombia.model.validatequestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class QuestionVerifyList {

    private String userId;
    private String productVersion;
    private String questionnaireId;
    private String parameterCode;
    private String questionnaireRecordId;
    private String identificationNumber;
    private String identificationType;
    private List<QuestionVerifyQuestionList> questionnaireAnswers;
}
