package co.com.bancolombia.validatequestion;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionVerifyList;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerify;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyRequest;
import co.com.bancolombia.model.validatequestion.QuestionnaireVerifyResponse;
import co.com.bancolombia.model.validatequestion.ValidateQuestion;
import co.com.bancolombia.model.validatequestion.gateways.QuestionnaireVerifyRestRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_VALIDATEQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONSUME_EXTERNAL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_TWO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_NAME_APPROVED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_NAME_REJECT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SUCCESFUL_RESULT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;

@RequiredArgsConstructor
public class ValidateQuestionUseCaseImpl implements ValidateQuestionUseCase {

    private final QuestionnaireVerifyRestRepository qRestRepository;
    private final ValidateQuestionSaveUseCase questionSaveUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final CoreFunctionDate coreFunctionDate;
    private final ApiRiskCredential apiRiskCredential;

    private final LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_IDENTITY, OPER_VALIDATE_QUESTIONS);

    @Override
    public QuestionnaireVerifyRequest createRequestVQuestionnaire(AcquisitionReply acquisition,
                                                                  QuestionVerifyList questionVerifyList) {
        questionVerifyList.setParameterCode(apiRiskCredential.getParameterCode());
        questionVerifyList.setProductVersion(apiRiskCredential.getProductVersion());
        questionVerifyList.setUserId(apiRiskCredential.getUserId());
        return QuestionnaireVerifyRequest.builder().data(Collections.singletonList(questionVerifyList)).build();
    }

    @Override
    public ValidateQuestion validateResponseQuestionnaire(QuestionnaireVerify response, AcquisitionReply acquisition,
                                                          InfoReuseCommon infoReuseCommon) {
        boolean validate = false;
        ValidateQuestion validateQuestion = null;
        if (!response.getVerificationApproval().isEmpty()) {
            validate = Boolean.parseBoolean(response.getVerificationApproval());
        }
        if (validate) {
            vinculationUpdateUseCase.markOperation(acquisition.getAcquisitionId(), CODE_VALIDATE_VALIDATEQUESTIONS,
                    Numbers.TWO.getNumber());
            validateQuestion = ValidateQuestion.builder().outComeCode(OUT_COME_CODE_ONE)
                    .outComeName(OUT_COME_NAME_APPROVED).infoReuseCommon(infoReuseCommon).build();
        } else {
            vinculationUpdateUseCase.markOperation(acquisition.getAcquisitionId(), CODE_VALIDATE_VALIDATEQUESTIONS,
                    Numbers.THREE.getNumber());
            vinculationUpdateUseCase.updateAcquisition(acquisition.getAcquisitionId(), Numbers.TWO.getNumber());
            validateQuestion = ValidateQuestion.builder().outComeCode(OUT_COME_CODE_TWO)
                    .outComeName(OUT_COME_NAME_REJECT).infoReuseCommon(infoReuseCommon).build();
        }
        return validateQuestion;
    }

    @Override
    public ValidateQuestion startProcessValidateQuestion(BasicAcquisitionRequest basicAcquisitionRequest,
                                                         AcquisitionReply acquisition,
                                                         QuestionVerifyList questionVerifyList) {
        QuestionnaireVerifyRequest request = this.createRequestVQuestionnaire(acquisition, questionVerifyList);
        adapter.info(CONSUME_EXTERNAL);
        QuestionnaireVerifyResponse response = qRestRepository.getUserQuestionnaireVerify(request,
                basicAcquisitionRequest.getMessageId(), coreFunctionDate.getDatetime());
        adapter.info(CONSUME_EXTERNAL_RESULT.concat(SUCCESFUL_RESULT));
        questionSaveUseCase.saveInfo(response, acquisition, basicAcquisitionRequest);
        return validateResponseQuestionnaire(response.getData().get(0), acquisition, response.getInfoReuseCommon());
    }
}
