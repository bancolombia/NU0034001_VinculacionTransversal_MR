package co.com.bancolombia.usecase.personalinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.BIRTHDATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EXPEDITION_DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FIRST_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FIRST_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SECOND_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SECOND_SURNAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_ID_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FIELD_ALREADY_EXTRACTED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE;

@RequiredArgsConstructor
public class PersonalInformationStartProcessUseCaseImpl implements PersonalInformationStartProcessUseCase {

    private final PersonalInformationUseCase personalInformationUseCase;
    private final PersonalInformationRepository pInfoRepository;
    private final MergeUseCase mergeUseCase;

    LoggerAdapter adapter = new LoggerAdapter(Constants.PRODUCT_VTN,
            Constants.CODE_PERSONAL_INFO, this.getClass().getName());

    @Override
    public PersonalInformation startProcessPersonalInformation(PersonalInformation pInfo) {
        adapter.info(START_OPERATION);
        List<ErrorField> errorsDates = new ArrayList<>();
        errorsDates.addAll(this.validationDate(pInfo.getExpeditionDate(), EXPEDITION_DATE));
        errorsDates.addAll(this.validationDate(pInfo.getBirthdate(), BIRTHDATE));
        if (!errorsDates.isEmpty()) {
            showException(ERROR_CODE_DATE, errorsDates);
        }

        PersonalInformation info = this.pInfoRepository.findByAcquisition(pInfo.getAcquisition());
        if (info != null) {
            boolean isRecordUpgradeable;
            List<ErrorField> udpErrorField;
            isRecordUpgradeable = validateFieldsPersonalInfoNull(info);
            if(isRecordUpgradeable){
                udpErrorField = validateIfRecordUpgradeable( validateIfRecordUpgradeableFirst(info,pInfo), info,pInfo);
                if (!udpErrorField.isEmpty()) {
                    showException(ERROR_CODE_FIELD_ALREADY_EXTRACTED, udpErrorField); }
            }
            MergeAttrib mergeAttrib = this.constructMergeObject(isRecordUpgradeable);
            udpErrorField = this.mergeUseCase.merge(info,pInfo,mergeAttrib);

            if (!udpErrorField.isEmpty()) {
                showException(ERROR_CODE_NON_MODIFIABLE, udpErrorField);
            }
            info = info.toBuilder().updatedBy(pInfo.getCreatedBy()).updatedDate(pInfo.getCreatedDate()).build();
        } else {
            info = pInfo;
        }
        return personalInformationUseCase.save(pInfo, info);
    }

    public void showException(String codeException, List<ErrorField> errorFields){
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        error.put(codeException, errorFields);
        adapter.error(ERROR+codeException+MIDDLE_SCREEN+errorFields);
        throw new ValidationException(error);
    }

    @Override
    public List<ErrorField> validationDate(Date dataDate, String field) {
        List<ErrorField> errors = new ArrayList<>();
        CoreFunctionDate coreFunctionDate = new CoreFunctionDate();
        if (dataDate != null &&
                dataDate.compareTo(coreFunctionDate.getDatetime()) > 0) {
            errors.add(ErrorField.builder().name(field).build());
        }
        return errors;
    }


    public MergeAttrib constructMergeObject(boolean isRecordUpgradeable) {
        return MergeAttrib.builder().isRecordUpgradeable(isRecordUpgradeable)
                .nameList(null).stepCode(CODE_PERSONAL_INFO).build();
    }

    public boolean validateFieldsPersonalInfoNull(PersonalInformation pInformation) {
        return (pInformation.getCellphone() == null && pInformation.getEmail() == null
                && pInformation.getExpeditionCountry() == null && pInformation.getExpeditionDepartment() == null
                && pInformation.getExpeditionPlace() == null );
    }

    public List<ErrorField> validateIfRecordUpgradeableFirst(
            PersonalInformation piOld, PersonalInformation piNew) {
        List<ErrorField> errorFields = new ArrayList<>();
        if(piOld.getFirstName() != null && piNew.getFirstName() != null){
            errorFields.add(ErrorField.builder().name(FIRST_NAME).complement(TYPE_ID_CC).build());
        }
        if(piOld.getSecondName() != null && piNew.getSecondName() != null){
            errorFields.add(ErrorField.builder().name(SECOND_NAME).complement(TYPE_ID_CC).build());
        }
        if(piOld.getFirstSurname() != null && piNew.getFirstSurname() != null){
            errorFields.add(ErrorField.builder().name(FIRST_SURNAME).complement(TYPE_ID_CC).build());
        }
        if(piOld.getSecondSurname()!=null && piNew.getSecondSurname()!=null){
            errorFields.add(ErrorField.builder().name(SECOND_SURNAME).complement(TYPE_ID_CC).build());
        }
        return errorFields;
    }

    public List<ErrorField> validateIfRecordUpgradeable(
            List<ErrorField> errorFields ,PersonalInformation piOld, PersonalInformation piNew) {
        if(piOld.getBirthdate()!=null && piNew.getBirthdate()!=null){
            errorFields.add(ErrorField.builder().name(BIRTHDATE).complement(TYPE_ID_CC).build());
        }

        if(piOld.getExpeditionDate()!=null && piNew.getExpeditionDate()!=null){
            errorFields.add(ErrorField.builder().name(EXPEDITION_DATE).complement(TYPE_ID_CC).build());
        }
        return errorFields;
    }
}