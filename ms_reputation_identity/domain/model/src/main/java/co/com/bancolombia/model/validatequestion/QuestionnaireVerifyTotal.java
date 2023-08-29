package co.com.bancolombia.model.validatequestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireVerifyTotal {

    private QuestionnaireVerifyResponse questionnaireVerifyResponse;
    private QuestionnaireVerifyErrorResponse errors;
}
