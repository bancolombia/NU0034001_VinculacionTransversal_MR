package co.com.bancolombia.api.identity;

import co.com.bancolombia.ReputationIdentityController;
import co.com.bancolombia.ResponseFactory;
import co.com.bancolombia.api.GenericStep;
import co.com.bancolombia.api.model.ValidateQuestionResponse;
import co.com.bancolombia.api.model.ValidateQuestionsRequest;
import co.com.bancolombia.api.model.ValidateQuestionsRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validatequestion.QuestionVerifyList;
import co.com.bancolombia.model.validatequestion.QuestionVerifyQuestionList;
import co.com.bancolombia.model.validatequestion.ValidateQuestion;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.validatequestion.ValidateQuestionUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_VALIDATEQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ReputationIdentityController
@Api(tags = {"AcquisitionIdentity",})
public class ValidateQuestionsController implements ValidateQuestionsOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private GenericStep genericStep;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private ValidateQuestionUseCase validateQuestionUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_IDENTITY, OPER_VALIDATE_QUESTIONS);

    public QuestionVerifyList createRequest(ValidateQuestionsRequest body, AcquisitionReply acquisition) {
        List<QuestionVerifyQuestionList> questionLists = new ArrayList<>();
        body.getData().getAnswerList().forEach(l -> questionLists.add(
                QuestionVerifyQuestionList.builder().questionId(l.getQuestionId()).answerId(l.getAnswerId()).build()));
        return QuestionVerifyList.builder().identificationNumber(acquisition.getDocumentNumber())
                .identificationType(acquisition.getDocumentTypeCodeGenericType())
                .questionnaireId(body.getData().getQuestionnaireId()).questionnaireAnswers(questionLists)
                .questionnaireRecordId(body.getData().getQuestionnaireRecordId()).build();
    }

    @Override
    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_VALIDATE_VALIDATEQUESTIONS)
    public ResponseEntity<ValidateQuestionResponse> validateQuestionsIdentity(
            @ApiParam(value = "Validate Questions Customer", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody ValidateQuestionsRequest body) {
        webRequest.setAttribute(META, body.getMeta(), SCOPE_REQUEST);
        UserInfoRequestData dataRequest = UserInfoRequestData.builder().acquisitionId(body.getData().getAcquisitionId())
                .documentType(body.getData().getDocumentType()).documentNumber(body.getData().getDocumentNumber())
                .build();
        adapter.info(START_OPERATION);
        StepForLogFunctional stepForLogFunctional = genericStep.firstStepForLogFunctional(dataRequest, body.getMeta(),
                CODE_VALIDATE_VALIDATEQUESTIONS);
        ValidateQuestionsRequestData data = body.getData();
        genericStep.validRequestQuestionList(body);
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(data.getAcquisitionId(),
                data.getDocumentType(), data.getDocumentNumber(), CODE_VALIDATE_VALIDATEQUESTIONS);
        QuestionVerifyList qList = createRequest(body, acquisitionReply);
        ValidateQuestion validateQuestion = validateQuestionUseCase.startProcessValidateQuestion(
                BasicAcquisitionRequest.builder().messageId(body.getMeta().getMessageId())
                        .userTransaction(body.getMeta().getUsrMod()).build(), acquisitionReply, qList);
        adapter.info(FINISH_OPERATION);
        genericStep.finallyStep(stepForLogFunctional, data.getAcquisitionId(), validateQuestion.getInfoReuseCommon(),
                CODE_VALIDATE_VALIDATEQUESTIONS);
        return new ResponseEntity<>(ResponseFactory.buildValidateQuestionnaireResponse(body, validateQuestion),
                HttpStatus.OK);
    }
}
