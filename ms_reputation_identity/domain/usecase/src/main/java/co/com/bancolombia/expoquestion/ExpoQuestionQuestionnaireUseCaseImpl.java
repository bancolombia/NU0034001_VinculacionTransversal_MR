package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireList;
import co.com.bancolombia.model.expoquestion.QuestionnaireRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.gateways.QuestionnaireRestRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_GENERATE_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INSUFFICIENT_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INSUFFICIENT_QUESTIONS_DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.LAST_CONSULT_INACTIVE_DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ATTEMPTS_DAY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ATTEMPTS_MONTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ATTEMPTS_YEAR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ENTERED_DAY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ENTERED_MONTH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MAX_ENTERED_YEAR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.UNAUTHORIZED_CONSULT_T1;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.UNAUTHORIZED_CONSULT_T1_DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_GENERATE_QUESTIONNAIRE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_SYSTEM;

@RequiredArgsConstructor
public class ExpoQuestionQuestionnaireUseCaseImpl implements ExpoQuestionQuestionnaireUseCase {

    private final ExpoQuestionSaveUseCase expoQuestionSUC;
    private final CoreFunctionDate coreFD;
    private final QuestionnaireRestRepository questionnaireRestRepository;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ExpoQuestionValidationUseCase expoQuestionVUC;
    private final ApiRiskCredential apiRiskCredential;

    private final LoggerAdapter adapter = new LoggerAdapter(Constants.SYSTEM_VTN, Constants.SERVICE_IDENTITY,
            Constants.OPER_EXPO_QUESTIONS_Q);

    @Override
    public QuestionnaireResponse startProcessQuestionnaire(AcquisitionReply acquisitionReply,
                                                           BasicAcquisitionRequest bARequest,
                                                           ValidateIdentificationResponse vIdentifiResponse,
                                                           String idExpoQuestion) {
        adapter.info(Constants.CONSUME_EXTERNAL);
        QuestionnaireRequest qRequest = createQuestionnaireRequest(acquisitionReply, vIdentifiResponse);
        QuestionnaireResponse questionnaireResponse = questionnaireRestRepository.getUserInfoQuestionnaire(
                qRequest, bARequest.getMessageId(), coreFD.getDatetime(), vIdentifiResponse.getInfoReuseCommon()
                        .getRequestReuse(), vIdentifiResponse.getInfoReuseCommon().getResponseReuse());
        adapter.info(Constants.CONSUME_EXTERNAL_RESULT.concat(Constants.QUESTIONNAIRE_RESULT +
                questionnaireResponse.getData().get(0).getQuestionnarieResult()));
        expoQuestionSUC.saveQuestionnaire(questionnaireResponse, idExpoQuestion);
        questionnaireResponse = this.actionValidation(acquisitionReply, questionnaireResponse);
        questionnaireResponse.getInfoReuseCommon().setDateRequestReuse(vIdentifiResponse.getInfoReuseCommon()
                .getDateRequestReuse());
        return questionnaireResponse;
    }

    @Override
    public QuestionnaireRequest createQuestionnaireRequest(AcquisitionReply acquisitionReply,
                                                           ValidateIdentificationResponse vIdentifiResponse) {
        List<QuestionnaireList> dataQuestionnaireRequest = Collections.singletonList(QuestionnaireList.builder()
                .identificationNumber(acquisitionReply.getDocumentNumber())
                .identificationType(acquisitionReply.getDocumentTypeCodeGenericType())
                .validationIdentifier(vIdentifiResponse.getData().get(0).getValidationIdentifier())
                .parameterCode(apiRiskCredential.getParameterCode())
                .productVersion(apiRiskCredential.getProductVersion()).userId(apiRiskCredential.getUserId()).build());
        return QuestionnaireRequest.builder().data(dataQuestionnaireRequest).build();
    }

    public QuestionnaireResponse actionValidation(AcquisitionReply acqReply,
                                                  QuestionnaireResponse questionnaireResponse) {
        String questionnaireResult = questionnaireResponse.getData().get(0).getQuestionnarieResult();
        String timeUnLock;
        switch (questionnaireResult) {
            case Constants.GENERATE_QUESTIONS_OK:
                return questionnaireResponse;
            case ERROR_GENERATE_QUESTIONS:
                valiStateAndErrors(acqReply, ERROR_CODE_GENERATE_QUESTIONNAIRE, ERROR_GENERATE_QUESTIONS, "");
                break;
            case INSUFFICIENT_QUESTIONS:
                valiStateAndErrors(acqReply, ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND, INSUFFICIENT_QUESTIONS,
                        INSUFFICIENT_QUESTIONS_DETAIL);
                break;
            case MAX_ATTEMPTS_DAY:
                timeUnLock = expoQuestionVUC.blockCustomer(acqReply, Constants.DAY);
                valiStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, MAX_ATTEMPTS_DAY, timeUnLock);
                break;
            case MAX_ATTEMPTS_MONTH:
                timeUnLock = expoQuestionVUC.blockCustomer(acqReply, Constants.MONTH);
                valiStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, MAX_ATTEMPTS_MONTH, timeUnLock);
                break;
            case MAX_ATTEMPTS_YEAR:
                timeUnLock = expoQuestionVUC.blockCustomer(acqReply, Constants.YEAR);
                valiStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, MAX_ATTEMPTS_YEAR, timeUnLock);
                break;
            default:
                actionValidationSecondary(acqReply, questionnaireResult);
        }
        return questionnaireResponse;
    }

    public void actionValidationSecondary(AcquisitionReply acqReply, String questionnaireResult) {
        String timeUnLock;
        switch (questionnaireResult) {
            case MAX_ENTERED_DAY:
                timeUnLock = expoQuestionVUC.blockCustomer(acqReply, Constants.DAY);
                valiStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, MAX_ENTERED_DAY, timeUnLock);
                break;
            case MAX_ENTERED_MONTH:
                timeUnLock = expoQuestionVUC.blockCustomer(acqReply, Constants.MONTH);
                valiStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, MAX_ENTERED_MONTH, timeUnLock);
                break;
            case MAX_ENTERED_YEAR:
                timeUnLock = expoQuestionVUC.blockCustomer(acqReply, Constants.YEAR);
                valiStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, MAX_ENTERED_YEAR, timeUnLock);
                break;
            case Constants.LAST_CONSULT_INACTIVE:
                valiStateAndErrors(acqReply, ERROR_CODE_SYSTEM, Constants.LAST_CONSULT_INACTIVE,
                        LAST_CONSULT_INACTIVE_DETAIL);
                break;
            case UNAUTHORIZED_CONSULT_T1:
                valiStateAndErrors(acqReply, ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND, UNAUTHORIZED_CONSULT_T1,
                        UNAUTHORIZED_CONSULT_T1_DETAIL);
                break;
            default:
                valiStateAndErrors(acqReply, ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND, "", "");
        }
    }

    @Override
    public void valiStateAndErrors(AcquisitionReply acquisitionReply, String exception, String code, String detail) {
        if (!ERROR_GENERATE_QUESTIONS.equals(code) && !Constants.LAST_CONSULT_INACTIVE.equals(code)) {
            vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(),
                    Constants.CODE_VALIDATE_EXPQUESTIONS, Numbers.THREE.getNumber());
            vinculationUpdateUseCase.updateAcquisition(acquisitionReply.getAcquisitionId(), Numbers.TWO.getNumber());
        }
        String nameValue;
        if (!code.equals(MAX_ATTEMPTS_DAY) && !code.equals(MAX_ATTEMPTS_MONTH) && !code.equals(MAX_ATTEMPTS_YEAR) &&
                !code.equals(MAX_ENTERED_DAY) && !code.equals(MAX_ENTERED_MONTH) && !code.equals(MAX_ENTERED_YEAR)) {
            nameValue = Constants.OPER_EXPO_QUESTIONS;
        } else {
            nameValue = detail;
        }
        expoQuestionVUC.validateException(exception, nameValue, code, detail);
    }

}
