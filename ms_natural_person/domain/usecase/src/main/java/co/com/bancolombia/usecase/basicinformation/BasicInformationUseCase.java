package co.com.bancolombia.usecase.basicinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.basicinformation.gateways.BasicInformationRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.ValidateMandatoryFields;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_BASIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_GENDER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_ID_CC;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FIELD_ALREADY_EXTRACTED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_MANDATORY;

@RequiredArgsConstructor
public class BasicInformationUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateCatalogsBasicUseCase validateCatalogsBasicUseCase;
    private final ValidateMandatoryFields validateMandatoryFields;
    private final MergeUseCase mergeUseCase;
    private final BasicInformationRepository basicInformationRepository;

    LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, CODE_BASIC_INFO, this.getClass().getName());

    public BasicInformation save(BasicInformation basicInformation) {
        return this.basicInformationRepository.save(basicInformation);
    }

    public BasicInformation startProcessBasicInformation(BasicInformation bInfo) {
        validateCatalogsBasicUseCase.validateBasicInfoCatalogs(bInfo);
        Acquisition acquisition = bInfo.getAcquisition();
        BasicInformation infoOld = basicInformationRepository.findByAcquisition(acquisition);
        if (infoOld != null) {
            boolean isRecordUpgradeable = validateFieldsBasicInfoNull(infoOld, bInfo);

            MergeAttrib mergeAttrib = constructMergeObject(isRecordUpgradeable);
            List<ErrorField> udpErrorField = mergeUseCase.merge(infoOld,bInfo,mergeAttrib);

            if (!udpErrorField.isEmpty()) {
                showException(ERROR_CODE_NON_MODIFIABLE,udpErrorField);
            }
            infoOld = infoOld.toBuilder().updatedBy(bInfo.getCreatedBy()).updatedDate(bInfo.getCreatedDate()).build();
        } else {
            infoOld = bInfo;
        }
        List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                .checkListStatus(acquisition.getId(), CODE_BASIC_INFO).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());

        validateMandatoryFields(infoOld, mandatoryExecFList);
        vinculationUpdateUseCase.markOperation(
                infoOld.getAcquisition().getId(), CODE_BASIC_INFO, CODE_ST_OPE_COMPLETADO);
        return save(infoOld);
    }

    public void validateMandatoryFields(BasicInformation info, List<ExecFieldReply> mandatoryExecFList) {
        List<ErrorField> eFields = validateMandatoryFields.validateMandatoryFields(info, mandatoryExecFList);
        if (eFields != null && !eFields.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_OPTIONAL_MANDATORY, eFields);
            adapter.error(ERROR + ERROR_CODE_OPTIONAL_MANDATORY + MIDDLE_SCREEN + eFields);
            throw new ValidationException(error);
        }
    }

    public Optional<BasicInformation> findByAcquisition(Acquisition acquisition){
        return  Optional.ofNullable(basicInformationRepository.findByAcquisition(acquisition));
    }

    public MergeAttrib constructMergeObject(boolean isRecordUpgradeable) {
        return MergeAttrib.builder().isRecordUpgradeable(isRecordUpgradeable)
                .nameList(null).stepCode(CODE_BASIC_INFO).build();
    }

    public boolean validateFieldsBasicInfoNull(BasicInformation bInfo1, BasicInformation bInfo2) {
        boolean assertion = false;
        if (bInfo1.getBirthCity() == null && bInfo1.getBirthDepartment() == null
                &&  bInfo1.getCountry() == null && validateFieldsBasicInfoNullComplement(bInfo1)
                && validateFieldsBasicInfoNullSupplementary(bInfo1)){
            List<ErrorField> udpErrorField = validateIfRecordUpgradeable(bInfo1, bInfo2);
            if (!udpErrorField.isEmpty()) {
                showException(ERROR_CODE_FIELD_ALREADY_EXTRACTED, udpErrorField);
            }
            assertion = true;
        }
        return assertion;
    }

    public List<ErrorField> validateIfRecordUpgradeable(
            BasicInformation basicInformationOld, BasicInformation basicInformationNew){
        List<ErrorField> errorFields = new ArrayList<>();
        if(basicInformationOld.getGender() != null && basicInformationNew.getGender() != null){
            errorFields.add(ErrorField.builder().name(F_GENDER).complement(TYPE_ID_CC).build());
        }
        return errorFields;
    }

    public boolean validateFieldsBasicInfoNullComplement(BasicInformation bInformation){
        return (bInformation.getEducationLevel() == null && bInformation.getHousingType() == null
                && bInformation.getEntryCompanyDate() == null && bInformation.getNationality() == null
                && bInformation.getPep() == null && bInformation.getSocialStratum() == null);
    }

    public boolean validateFieldsBasicInfoNullSupplementary(BasicInformation bInformation){
        return ( bInformation.getCivilStatus() == null && bInformation.getContractType() == null
                && bInformation.getDependants() == null);
    }

    public void showException(String codeException, List<ErrorField> errorFields){
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        error.put(codeException, errorFields);
        adapter.error(ERROR + codeException + MIDDLE_SCREEN + errorFields);
        throw new ValidationException(error);
    }
}