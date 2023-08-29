package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationList;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationRequest;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.gateways.ValidateIdentificationRestRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ATTEMPTS_ALLOWED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_EXPQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DAY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INVALID_IDENTIFICATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_DATA_MATCH;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_DATA_MATCH_DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_EXIST_IDENTIFICATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NO_EXIST_IDENTIFICATION_DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_EXPO_QUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_VIDENTITY_BACKEND;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VALIDATE_IDENTITY_CODE_ERROR_DOCUMENT_NOT_VIGENT;

@RequiredArgsConstructor
public class ExpoQuestionIdentificationUseCaseImpl implements ExpoQuestionIdentificationUseCase {

    private final ValidateIdentificationRestRepository vIdentificationRestRepository;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ExpoQuestionQuestionnaireUseCase questionnaireUC;
    private final ExpoQuestionSaveUseCase expoQuestionSUC;
    private final ExpoQuestionValidationUseCase expoQuestionVUC;
    private final ApiRiskCredential apiRiskCredential;

    private final LoggerAdapter adapter = new LoggerAdapter(Constants.SYSTEM_VTN, Constants.SERVICE_IDENTITY,
            Constants.OPER_EXPO_QUESTIONS_I);

    @Override
    public QuestionnaireResponse starProcessVIdentification(AcquisitionReply acqReply,
                                                            BasicAcquisitionRequest bARequest) {
        expoQuestionVUC.validationStageOne(acqReply);
        ValidateIdentityReply persoInfo = expoQuestionVUC.validateMissingData(acqReply);
        adapter.info(Constants.CONSUME_EXTERNAL);
        ValidateIdentificationRequest vIdentificationRequest = createVIdentifiRequest(acqReply, persoInfo);
        ValidateIdentificationResponse vIdentifiResponse = vIdentificationRestRepository
                .getUserInfoIdentification(vIdentificationRequest, bARequest.getMessageId());
        adapter.info(Constants.CONSUME_EXTERNAL_RESULT.concat(Constants.RESULT_CODE_VALIDATION +
                vIdentifiResponse.getData().get(0).getResultCodeValidation()));
        String idExpoQuestionSave = expoQuestionSUC.saveIdentification(acqReply, bARequest,
                vIdentifiResponse);
        return actionValidation(acqReply, bARequest, vIdentifiResponse, idExpoQuestionSave);
    }

    @Override
    public ValidateIdentificationRequest createVIdentifiRequest(AcquisitionReply acqReply,
                                                                ValidateIdentityReply persoInfo) {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        String strDate = dateFormat.format(persoInfo.getExpeditionDate());
        List<ValidateIdentificationList> dataVIdentifiRequest = Collections.singletonList(
                ValidateIdentificationList.builder().userId(apiRiskCredential.getUserId())
                        .parameterCode(apiRiskCredential.getParameterCode())
                        .productVersion(apiRiskCredential.getProductVersion())
                        .identificationNumber(acqReply.getDocumentNumber())
                        .identificationType(acqReply.getDocumentTypeCodeGenericType())
                        .names(persoInfo.getFirstName() + SPACE + persoInfo.getSecondName())
                        .surname(persoInfo.getFirstSurname()).secondSurname(persoInfo.getSecondSurname())
                        .dateOfIssue(strDate).build());
        return ValidateIdentificationRequest.builder().data(dataVIdentifiRequest).build();
    }

    @Override
    public QuestionnaireResponse actionValidation(AcquisitionReply acqReply, BasicAcquisitionRequest bARequest,
                                                  ValidateIdentificationResponse vIdentifiResponse, String id) {
        String codeValidationResult = vIdentifiResponse.getData().get(0).getResultCodeValidation();
        switch (codeValidationResult) {
            case Constants.CREDIT_LIST_HISTORY:
            case Constants.NO_CREDIT_LIST_HISTORY:
                vinculationUpdateUseCase.markOperation(acqReply.getAcquisitionId(), CODE_VALIDATE_EXPQUESTIONS,
                        Numbers.TWO.getNumber());
                return questionnaireUC.startProcessQuestionnaire(acqReply, bARequest, vIdentifiResponse, id);
            case NO_DATA_MATCH:
                validateStateAndErrors(acqReply, ERROR_CODE_VIDENTITY_BACKEND, NO_DATA_MATCH, NO_DATA_MATCH_DETAIL);
                break;
            case ATTEMPTS_ALLOWED:
                String timeUnBlock = expoQuestionVUC.blockCustomer(acqReply, DAY);
                validateStateAndErrors(acqReply, ERROR_CODE_NUMBER_ATTEMPTS_ALLOWED, ATTEMPTS_ALLOWED, timeUnBlock);
                break;
            case NO_EXIST_IDENTIFICATION:
                validateStateAndErrors(acqReply, ERROR_CODE_VIDENTITY_BACKEND, NO_EXIST_IDENTIFICATION,
                        NO_EXIST_IDENTIFICATION_DETAIL);
                break;
            case INVALID_IDENTIFICATION:
                validateStateAndErrors(acqReply, VALIDATE_IDENTITY_CODE_ERROR_DOCUMENT_NOT_VIGENT, EMPTY, EMPTY);
                break;
            default:
                validateStateAndErrors(acqReply, ERROR_CODE_VIDENTITY_BACKEND, EMPTY, EMPTY);
        }
        return null;
    }

    @Override
    public void validateStateAndErrors(AcquisitionReply acqReply, String exception, String code, String detail) {
        vinculationUpdateUseCase.markOperation(acqReply.getAcquisitionId(), CODE_VALIDATE_EXPQUESTIONS,
                Numbers.THREE.getNumber());
        vinculationUpdateUseCase.updateAcquisition(acqReply.getAcquisitionId(), Numbers.TWO.getNumber());
        String nameValue;
        if (!code.equals(ATTEMPTS_ALLOWED)) {
            nameValue = OPER_EXPO_QUESTIONS;
        } else {
            nameValue = detail;
        }
        expoQuestionVUC.validateException(exception, nameValue, code, detail);
    }
}
