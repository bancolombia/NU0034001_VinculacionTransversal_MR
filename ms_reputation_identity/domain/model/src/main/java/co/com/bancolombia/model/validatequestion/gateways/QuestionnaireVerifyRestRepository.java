package co.com.bancolombia.model.validatequestion.gateways;

import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyRequest;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;

import java.util.Date;

public interface QuestionnaireVerifyRestRepository {

    QuestionnaireVerifyResponse getUserQuestionnaireVerify(QuestionnaireVerifyRequest request,
                                                           String messageId, Date dateRequestApi);
}
