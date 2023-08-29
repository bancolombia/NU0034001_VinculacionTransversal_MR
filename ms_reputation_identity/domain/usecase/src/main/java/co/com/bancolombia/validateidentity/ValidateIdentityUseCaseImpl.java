package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.validateidentity.ErrorItemValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentityRequest;
import co.com.bancolombia.model.validateidentity.ValidateIdentityResponseError;
import co.com.bancolombia.model.validateidentity.ValidateIdentityTotalResponse;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityRestRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ELEMENT_FOUND_DATE_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ELEMENT_FOUND_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NAME_SERVICE_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_INCOMPLETE_FIELDS_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NOT_FOUND_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.TITLE;

@RequiredArgsConstructor
public class ValidateIdentityUseCaseImpl implements ValidateIdentityUseCase {

    private final NaturalPersonUseCase naturalUseCase;
    private final ValidateIdentityRestRepository validateIdentityRestRepository;
    private final CoreFunctionDate coreFunctionDate;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateIdentityRuleUseCase vIdentityRuleUseCase;
    private final ValidateIdentitySaveUseCase identitySaveUseCase;

    @Override
    public ValidateIdentity startProcessValidateIdentity(AcquisitionReply acquisitionReply,
                                                         BasicAcquisitionRequest bRequest) {
        ValidateIdentityReply validateReply = naturalUseCase.validateIdentity(acquisitionReply.getAcquisitionId());
        String firstSurnameFound = getFirstSurname(validateReply);
        ValidateIdentityRequest validateIdentityRequest = ValidateIdentityRequest.builder()
                .firstSurname(firstSurnameFound).document(acquisitionReply.getDocumentTypeOrderExperian()
                        .concat(MIDDLE_SCREEN).concat(acquisitionReply.getDocumentNumber())).build();
        ValidateIdentityTotalResponse response = validateIdentityRestRepository.getUserInfoValidateIdentity(
                validateIdentityRequest, bRequest.getMessageId(), coreFunctionDate.getDatetime());
        ValidateIdentity validateIdentity = null;
        if (response.getValidateIdentityResponse() == null) {
            this.acquisitionNotFoundValidate(acquisitionReply, response.getErrors());
        } else {
            validateIdentity = vIdentityRuleUseCase.startValidateIdentityRule(identitySaveUseCase
                            .save(response.getValidateIdentityResponse(), acquisitionReply, bRequest), validateReply,
                    acquisitionReply, vinculationUpdateUseCase.validateRules(acquisitionReply.getAcquisitionId()));
            InfoReuseCommon infoReuseCommon = response.getInfoReuseCommon();
            HashMap<String, String> map = new HashMap<>();
            map.put("isDocumentManual", vIdentityRuleUseCase
                    .convertBooleanString(validateIdentity.getDocumentManual()));
            map.put("isEmailAndCellError", vIdentityRuleUseCase
                    .convertBooleanString(validateIdentity.getEmailAndCellError()));
            infoReuseCommon.setMapFields(map);
            validateIdentity.setInfoReuseCommon(infoReuseCommon);
        }
        return validateIdentity;
    }

    @Override
    public String getFirstSurname(ValidateIdentityReply validateReply) {
        List<ErrorField> list = new ArrayList<>();
        if (validateReply.getFirstSurname() == null) {
            list.add(ErrorField.builder().name(ELEMENT_FOUND_NAME).build());
        }
        if (validateReply.getCellphonePersonal() == null) {
            list.add(ErrorField.builder().name("Celular").build());
        }
        if (validateReply.getEmailPersonal() == null) {
            list.add(ErrorField.builder().name("Email").build());
        }
        if (validateReply.getExpeditionDate() == null) {
            list.add(ErrorField.builder().name(ELEMENT_FOUND_DATE_EXP).build());
        }
        if (validateReply.getBirthDate() == null) {
            list.add(ErrorField.builder().name("Fecha de nacimiento").build());
        }
        if (!list.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_INCOMPLETE_FIELDS_IDENTITY, list);
            throw new ValidationException(error);
        }
        return validateReply.getFirstSurname();
    }

    @Override
    public void acquisitionNotFoundValidate(AcquisitionReply acquisitionReply, ValidateIdentityResponseError vIError) {
        vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(), CODE_VALIDATE_IDENTITY,
                Numbers.FOUR.getNumber());
        ErrorItemValidateIdentity errorItemVI = vIError.getErrors().get(0);
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        error.put(ERROR_CODE_NOT_FOUND_VALIDATE_IDENTITY,
                Collections.singletonList(ErrorField.builder().name(NAME_SERVICE_VALIDATE_IDENTITY)
                        .complement(CODE_ERROR.concat(errorItemVI.getCode()).concat(SPACE).concat(TITLE)
                                .concat(vIError.getTitle()).concat(SPACE).concat(DETAIL)
                                .concat(errorItemVI.getDetail())).build()));
        throw new ValidationException(error);
    }
}
