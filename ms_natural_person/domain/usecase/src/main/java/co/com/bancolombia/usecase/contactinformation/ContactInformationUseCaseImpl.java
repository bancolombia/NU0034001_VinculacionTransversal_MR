package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.contactinformation.generic.ArrayErrors;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTACT_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_CONTROLADO_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CONTACT_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE_LIST;

@RequiredArgsConstructor
public class ContactInformationUseCaseImpl implements ContactInformationUseCase {

    private final MergeUseCase mergeUseCase;
    private final PersonalInformationUseCase perInfoUseCase;
    private final ContactInformationUseCasePersist cInfoUCPer;
    private final ContactInformationProcessUseCase cInfoProcUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_IDENTITY, OPER_CONTACT_INF);

    @Override
    public List<ContactInformation> firstStepStartProcess(
            Acquisition a, List<ContactInformation> ciRe, ArrayErrors<ContactInformation> errorsList) {
        return cInfoProcUseCase.firstStepStartProcess(a, ciRe, errorsList);
    }

    @Override
    public String concatErrorTypeDirection(String tpDirection){
        return cInfoProcUseCase.concatErrorTypeDirection(tpDirection);
    }

    public ContactInformation constructContactInformation(ContactInformation cI) {
        return ContactInformation.builder().acquisition(cI.getAcquisition()).phone(cI.getPhone())
                .addressType(cI.getAddressType()).address(cI.getAddress()).companyName(cI.getCompanyName())
                .brand(cI.getBrand()).city(cI.getCity()).country(cI.getCountry()).department(cI.getDepartment())
                .ext(cI.getExt()).neighborhood(cI.getNeighborhood()).updatedBy(cI.getCreatedBy())
                .updatedDate(new CoreFunctionDate().getDatetime()).email(cI.getEmail()).cellphone(cI.getCellphone())
                .createdBy(cI.getCreatedBy()).createdDate(new CoreFunctionDate().getDatetime()).build();
    }

    public List<ExecFieldReply> mandatoryExecFList(Acquisition acquisition) {
        return this.vinculationUpdateUseCase.checkListStatus(acquisition.getId(), CODE_CONTACT_INFO)
                .getExecFieldList().stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());
    }

    public Optional<ContactInformation> filterAddress(List<ContactInformation> ciList, ContactInformation ad) {
        return ciList.stream().filter(predicate -> predicate.getAddressType().equals(ad.getAddressType())).findFirst();
    }

    public ContactInformation changeUpdateOrCreateBy(ContactInformation ci, ContactInformation oldNotUpdate) {
        if (oldNotUpdate != null && oldNotUpdate.getCreatedBy() != null && oldNotUpdate.getCreatedDate() != null) {
            ci.setCreatedBy(oldNotUpdate.getCreatedBy());
            ci.setCreatedDate(oldNotUpdate.getCreatedDate());
        } else {
            ci.setUpdatedBy(null);
            ci.setUpdatedDate(null);
        }
        return ci;
    }

    public void mergeFieldsContactInfo(ContactInformation infoSavedInDb, ContactInformation contInformationNew) {
        MergeAttrib mergeAttrib = MergeAttrib.builder().stepCode(CODE_CONTACT_INFO)
                .nameList(concatErrorTypeDirection(contInformationNew.getAddressType())).stepCode(CODE_CONTACT_INFO)
                .isRecordUpgradeable(false).build();
        List<ErrorField> upErrField = this.mergeUseCase.merge(infoSavedInDb, contInformationNew, mergeAttrib);
        if (!upErrField.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_NON_MODIFIABLE_LIST, upErrField);
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
    }

    public List<ContactInformation> updateInfoObjectNew(List<ContactInformation> cInfo) {
        List<ContactInformation> cInfoFinal = new ArrayList<>();
        cInfo.stream().forEach(infoItem -> {
            ContactInformation infoBd = this.cInfoUCPer.getContactInformationRepository()
                    .findByAcquisitionAndAddressType(infoItem.getAcquisition(), infoItem.getAddressType());
            if (infoBd != null && infoBd.getBrand() != null) {

                mergeFieldsContactInfo(infoBd, infoItem);
                infoBd.setAcquisition(infoItem.getAcquisition());
                infoBd.setUpdatedBy(infoItem.getCreatedBy());
                infoBd.setUpdatedDate(infoItem.getCreatedDate());
            } else {
                infoBd = infoItem;
            }
            cInfoFinal.add(infoBd);
        });
        return cInfoFinal;
    }

    public MergeAttrib constructMergeObject(boolean isRecordUpgradeable, ContactInformation ci) {
        return MergeAttrib.builder().isRecordUpgradeable(isRecordUpgradeable)
                .nameList(concatErrorTypeDirection(ci.getAddressType())).stepCode(CODE_CONTACT_INFO).build();
    }

    public ArrayErrors<ContactInformation> eachListContactInformation(List<ContactInformation> cInfo,
                                                                       ArrayErrors<ContactInformation> objList) {
        ArrayErrors<ContactInformation> result = objList;

        List<ContactInformation> cList = this.firstStepStartProcess(cInfo.get(0).getAcquisition(), cInfo, result);
        Optional<PersonalInformation> piOld = perInfoUseCase.findByAcquisition(cInfo.get(0).getAcquisition());
        cInfo.forEach(cI -> {
            ContactInformation ad = constructContactInformation(cI);
            Optional<ContactInformation> ci = this.filterAddress(cList, ad);
            boolean isRecordUpgradeable = false;
            if (ci.isPresent()) {
                isRecordUpgradeable = cInfoUCPer.getCValidationUseCase().validateFieldsContactInfoNull(ci.get());
                if (cI.getAddressType().equals(CO_ADDRESS_TYPE_RES) && piOld.isPresent()) {
                    if (!isRecordUpgradeable) {
                        result.errorFieldsMerge
                                .addAll(cInfoUCPer.getCValidationUseCase().validateIfRecordUpgradeable(piOld, ci));
                    } else {
                        ad.setEmail(piOld.get().getEmail());
                        ad.setCellphone(piOld.get().getCellphone());
                    }
                }
                MergeAttrib mergeAttrib = this.constructMergeObject(isRecordUpgradeable, ci.get());

                result.errorFieldsMerge.addAll(this.mergeUseCase.merge(ci.get(), ad, mergeAttrib));
                ad.setId(ci.get().getId());
                ad = this.changeUpdateOrCreateBy(ad, ci.get());
            }
            result.cInfoUpd.add(this.changeUpdateOrCreateBy(ad, null));
        });

        return result;
    }

    @Override
    public List<ContactInformation> startProcessContactInformation(List<ContactInformation> cInfo) {
        List<ContactInformation> cInfoFinal = this.updateInfoObjectNew(cInfo);
        ArrayErrors<ContactInformation> objList = new ArrayErrors<>(
                mandatoryExecFList(cInfoFinal.get(0).getAcquisition()));
        ArrayErrors<ContactInformation> objListReturn = eachListContactInformation(cInfoFinal, objList);
        cInfoUCPer.getCValidationUseCase().validateIfErrorField(objList.errorFieldsValidate, objList.errorFieldsMerge);
        return objListReturn.cInfoUpd;
    }

    @Override
    public ContactInformation save(ContactInformation contactInformation) {
        return cInfoUCPer.save(contactInformation);
    }

    @Override
    public List<ContactInformation> save(List<ContactInformation> contactInformationList) {
        return cInfoUCPer.save(contactInformationList);
    }
}