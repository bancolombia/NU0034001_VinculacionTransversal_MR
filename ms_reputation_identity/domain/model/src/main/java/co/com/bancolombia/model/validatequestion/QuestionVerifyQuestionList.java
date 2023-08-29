package co.com.bancolombia.model.validatequestion;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class QuestionVerifyQuestionList {

    private String questionId;
    private String answerId;
}
