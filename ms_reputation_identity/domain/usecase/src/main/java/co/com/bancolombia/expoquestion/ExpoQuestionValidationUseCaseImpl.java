package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_EXPQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_VALIDATEQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ELEMENT_FOUND_DATE_EXP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ELEMENT_FOUND_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ELEMENT_FOUND_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_INCOMPLETE_FIELDS_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_STAGE_COMPLETE_AUTO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_STAGE_PENDING;

@RequiredArgsConstructor
public class ExpoQuestionValidationUseCaseImpl implements ExpoQuestionValidationUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final NaturalPersonUseCase naturalPersonUseCase;
    private final CoreFunctionDate coreFD;

    @Override
    public void validationStageOne(AcquisitionReply acquisitionReply) {
        ChecklistReply identity = vinculationUpdateUseCase.checkListStatus(acquisitionReply.getAcquisitionId(),
                CODE_VALIDATE_IDENTITY);
        ChecklistReply expo = vinculationUpdateUseCase.checkListStatus(acquisitionReply.getAcquisitionId(),
                CODE_VALIDATE_EXPQUESTIONS);
        ChecklistReply valQuestion = vinculationUpdateUseCase.checkListStatus(acquisitionReply.getAcquisitionId(),
                CODE_VALIDATE_VALIDATEQUESTIONS);
        if (Numbers.ONE.getNumber().equals(identity.getStateOperation())) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_STAGE_PENDING,
                    Collections.singletonList(ErrorField.builder().name(OPER_VALIDATE_IDENTITY).build()));
            throw new ValidationException(error);
        } else if (Numbers.TWO.getNumber().equals(identity.getStateOperation()) && Numbers.SIX.getNumber()
                .equals(expo.getStateOperation()) && Numbers.SIX.getNumber().equals(valQuestion.getStateOperation())) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_STAGE_COMPLETE_AUTO, Collections.singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
    }

    @Override
    public ValidateIdentityReply validateMissingData(AcquisitionReply acquisitionReply) {
        ValidateIdentityReply personalInformation = naturalPersonUseCase
                .validateIdentity(acquisitionReply.getAcquisitionId());
        List<ErrorField> list = new ArrayList<>();
        if (personalInformation.getFirstName() == null) {
            list.add(ErrorField.builder().name(ELEMENT_FOUND_NAME).build());
        }
        if (personalInformation.getFirstSurname() == null) {
            list.add(ErrorField.builder().name(ELEMENT_FOUND_SURNAME).build());
        }
        if (personalInformation.getExpeditionDate() == null) {
            list.add(ErrorField.builder().name(ELEMENT_FOUND_DATE_EXP).build());
        }
        if (!list.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_INCOMPLETE_FIELDS_IDENTITY, list);
            throw new ValidationException(error);
        }
        return personalInformation;
    }

    @Override
    public void validateException(String exception, String nameValue, String code, String detail) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        ErrorField errorField = ErrorField.builder().name(nameValue)
                .complement(CODE_ERROR.concat(code).concat(SPACE)
                        .concat(DETAIL).concat(detail)).build();
        List<ErrorField> eFieldList = new ArrayList<>();
        eFieldList.add(errorField);
        error.put(exception, eFieldList);
        throw new ValidationException(error);
    }

    @Override
    public String blockCustomer(AcquisitionReply acquisitionReply, String code) {
        Date unBlockTime = coreFD.timeUnBlockCustomer(code);
        vinculationUpdateUseCase.blockCustomer(acquisitionReply.getDocumentType(),
                acquisitionReply.getDocumentNumber(), unBlockTime, CODE_VALIDATE_EXPQUESTIONS);
        return coreFD.compareDifferenceTime(unBlockTime, null, false, true);
    }
}
