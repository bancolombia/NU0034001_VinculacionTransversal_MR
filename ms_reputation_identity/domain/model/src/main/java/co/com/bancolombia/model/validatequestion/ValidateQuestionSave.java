package co.com.bancolombia.model.validatequestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ValidateQuestionSave {

    private String id;
    private String idAcquisition;
    private String verificationResult;
    private String questionnaireId;
    private String securityCode;
    private String verificationApproval;
    private String completedQuestions;
    private String fullyApprovedQuestionnaire;
    private String approvedMinimumQuestions;
    private String validationScore;
    private String messageId;
    private String requestDate;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
    private List<VerifyAnswers> verifyAnswers;
}
