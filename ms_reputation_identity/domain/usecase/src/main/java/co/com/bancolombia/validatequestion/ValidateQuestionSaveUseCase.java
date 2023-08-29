package co.com.bancolombia.validatequestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;

public interface ValidateQuestionSaveUseCase {

    void saveInfo(QuestionnaireVerifyResponse response, AcquisitionReply acquisition,
                  BasicAcquisitionRequest request);
}
