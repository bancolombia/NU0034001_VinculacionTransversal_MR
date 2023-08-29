package co.com.bancolombia.validatequestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionVerifyList;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerify;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyRequest;
import co.com.bancolombia.model.validatequestion.ValidateQuestion;

public interface ValidateQuestionUseCase {

    ValidateQuestion startProcessValidateQuestion(BasicAcquisitionRequest basicAcquisitionRequest,
                                                  AcquisitionReply acquisition,
                                                  QuestionVerifyList questionVerifyList);

    QuestionnaireVerifyRequest createRequestVQuestionnaire(AcquisitionReply acquisition,
                                                           QuestionVerifyList questionVerifyList);

    ValidateQuestion validateResponseQuestionnaire(QuestionnaireVerify response, AcquisitionReply acquisition,
                                                   InfoReuseCommon infoReuseCommon);
}
