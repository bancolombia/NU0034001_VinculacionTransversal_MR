package co.com.bancolombia.model.validatequestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireVerify {

    private String verificationResult;
    private String questionnaireId;
    private String securityCode;
    private String verificationApproval;
    private String completedQuestions;
    private String fullyApprovedQuestionnaire;
    private String approvedMinimumQuestions;
    private String validationScore;
    private List<QuestionnaireAnswer> questionnaireAnswers;
}
