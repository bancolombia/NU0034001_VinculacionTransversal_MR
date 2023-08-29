package co.com.bancolombia.model.validatequestion;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class VerifyAnswers {

    private String questionId;
    private String correctQuestion;
    private String correctAnswer;
}
