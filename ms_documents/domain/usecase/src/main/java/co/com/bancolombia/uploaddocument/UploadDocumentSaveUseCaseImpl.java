package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
import co.com.bancolombia.model.uploaddocument.UploadDocumentTotal;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BIRTHDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO_PARCIAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_SAVE_BASIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_SAVE_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EXPEDITION_DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.GENDER_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_DIGITALIZATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TIMESTAMP_WITH_MILI_NUMERIC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsRegex.REGEX_LETTERS_WITH_SPACES;

@RequiredArgsConstructor
public class UploadDocumentSaveUseCaseImpl implements UploadDocumentSaveUseCase {

    private final CoreFunctionDate coreFunctionDate;
    private final NaturalPersonUseCase naturalPersonUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_DIGITALIZATION, OPER_UPLOAD_DOCUMENT);

    @Override
    public void savePersonalInfo(KofaxInformation kofaxInformation, Acquisition acquisition, String usrTransaction) {
        EmptyReply reply = naturalPersonUseCase.savePersonalInfo(acquisition.getId(), kofaxInformation, usrTransaction);
        if (!reply.isValid()) {
            adapter.error(ERROR_SAVE_PERSONAL_INFO);
        }
    }

    @Override
    public void saveBasicInfo(
            KofaxInformation kofaxInformation, Acquisition acquisition, String basicInfGender, String usrTransaction) {

        EmptyReply reply = naturalPersonUseCase.saveBasicInfo(acquisition.getId(), basicInfGender, usrTransaction);
        if (reply.isValid()) {
            vinculationUpdateUseCase.markOperation(
                    acquisition.getId(), CODE_PROCESS_DOCUMENTS, CODE_ST_OPE_COMPLETADO_PARCIAL);
        } else {
            adapter.error(ERROR_SAVE_BASIC_INFO);
        }
    }

    @Override
    public String transformKofaxGenderField (String kofaxGender) {
        CatalogReply reply = vinculationUpdateUseCase.findCatalog(GENDER_CODE.concat(kofaxGender), GENDER);
        return reply.isValid() ? reply.getCode() : EMPTY;
    }

    @Override
    public boolean validateQualityFields(KofaxInformation information) {
        List<ErrorField> errorsFields = new ArrayList<>();

        errorsFields.addAll(isValidField(information.getFirstName(), REGEX_LETTERS_WITH_SPACES));
        errorsFields.addAll(isValidField(information.getSecondName(), REGEX_LETTERS_WITH_SPACES));
        errorsFields.addAll(isValidField(information.getFirstSurname(), REGEX_LETTERS_WITH_SPACES));
        errorsFields.addAll(isValidField(information.getSecondSurname(), REGEX_LETTERS_WITH_SPACES));
        errorsFields.addAll(validationDate(information.getExpeditionDate(), EXPEDITION_DATE));
        errorsFields.addAll(validationDate(information.getBirthDate(), BIRTHDATE));

        return errorsFields.isEmpty();
    }

    @Override
    public List<ErrorField> isValidField(String fieldValue, String regex) {
        List<ErrorField> errors = new ArrayList<>();

        if (fieldValue == null || fieldValue.isEmpty()) {
            return errors;
        }

        if (!fieldValue.matches(regex)) {
            errors.add(ErrorField.builder().name(fieldValue).build());
        }
        return errors;
    }

    @Override
    public boolean validateKofaxMessageId(UploadDocumentTotal uploadDocumentTotal, String messageId) {
        String kofaxMessageId;
        if (uploadDocumentTotal.getUploadDocumentResponse() != null) {
            kofaxMessageId = uploadDocumentTotal.getUploadDocumentResponse().getMeta().getMessageId();
        } else {
            kofaxMessageId = uploadDocumentTotal.getUploadDocumentErrorResponse().getMeta().getMessageId();
        }
        return kofaxMessageId.equals(messageId);
    }

    @Override
    public String getMessageId() {
        return new SimpleDateFormat(TIMESTAMP_WITH_MILI_NUMERIC).format(coreFunctionDate.toDate(LocalDateTime.now()));
    }

    private List<ErrorField> validationDate(Date dataDate, String field) {
        List<ErrorField> errors = new ArrayList<>();
        if (dataDate != null && dataDate.compareTo(coreFunctionDate.getDatetime()) > 0) {
            errors.add(ErrorField.builder().name(field).build());
        }
        return errors;
    }
}
