package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTACT_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_RECHAZADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONCAT_TYPE_ADDRESS_F;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONCAT_TYPE_ADDRESS_I;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_BRAND_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_CONTROLADO_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CONTACT_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;

public class ContactValidationUseCaseImpl extends
        ValidateInfoGeneric<ContactInformation, ExecFieldReply, DependentFieldUseCase>
        implements ContactIValidationUseCase {

    private final ContactInformationRepository contactInformationRepository;
    private final ParametersUseCase parametersUseCase;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_CONTACT_INF);

    public ContactValidationUseCaseImpl(DependentFieldUseCase dependentFieldUseCase,
                                        ContactInformationRepository contactInformationRepository,
                                        ParametersUseCase parametersUseCase,
                                        VinculationUpdateUseCase vinculationUpdateUseCase) {
        super(dependentFieldUseCase);
        this.contactInformationRepository = contactInformationRepository;
        this.parametersUseCase = parametersUseCase;
        this.vinculationUpdateUseCase = vinculationUpdateUseCase;
    }

    @Override
    public void validateIfErrorField(List<ErrorField> efMandatory, List<ErrorField> efMerge) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (!efMerge.isEmpty()) {
            error.put(ConstantsErrors.ERROR_CODE_NON_MODIFIABLE_LIST, efMerge);
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
        if (!efMandatory.isEmpty()) {
            error.put(ConstantsErrors.ERROR_CODE_OPTIONAL_MANDATORY_LIST, efMandatory);
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
    }

    @Override
    public boolean validateFirstFieldsContactInfoNull(ContactInformation contactInformation) {
        return (contactInformation.getUpdatedBy() == null && contactInformation.getUpdatedDate() == null
                && contactInformation.getBrand() == null && contactInformation.getCompanyName() == null
                && contactInformation.getAddress() == null && contactInformation.getNeighborhood() == null);
    }

    @Override
    public boolean validateFieldsContactInfoNull(ContactInformation contactInformation) {
        boolean secondValidation = contactInformation.getCity() == null && contactInformation.getDepartment() == null
                && contactInformation.getCountry() == null && contactInformation.getPhone() == null
                && contactInformation.getExt() == null;
        return (validateFirstFieldsContactInfoNull(contactInformation) && secondValidation);
    }

    @Override
    public List<ErrorField> validateIfRecordUpgradeable(
            Optional<PersonalInformation> piOld, Optional<ContactInformation> ci) {
        List<ErrorField> errorFields = new ArrayList<>();
        if (piOld.isPresent() && ci.isPresent()) {
            if (!piOld.get().getEmail().equals(ci.get().getEmail())) {
                errorFields.add(ErrorField.builder().name(EMAIL)
                        .complement(CONCAT_TYPE_ADDRESS_I + ci.get().getAddressType() + CONCAT_TYPE_ADDRESS_F).build());
            }
            if (!piOld.get().getCellphone().equals(ci.get().getCellphone())) {
                errorFields.add(ErrorField.builder().name(CELLPHONE)
                        .complement(CONCAT_TYPE_ADDRESS_I + ci.get().getAddressType() + CONCAT_TYPE_ADDRESS_F).build());
            }
        }
        return errorFields;
    }

    @Override
    public void validateForeignCountry(List<ContactInformation> contactInfo) {
        List<Parameters> parCountry = this.parametersUseCase.findByParent(PARAMETER_COUNTRY);
        if (!parCountry.isEmpty()) {
            contactInfo.forEach(c -> {
                if (c.getAddressType().equals(CO_ADDRESS_TYPE_RES)
                        && parCountry.stream().filter(fil -> fil.getCode().equals(c.getCountry())).count() == 0) {
                    this.vinculationUpdateUseCase.markOperation
                            (c.getAcquisition().getId(), CODE_CONTACT_INFO, CODE_ST_OPE_RECHAZADO);
                    vinculationUpdateUseCase.updateAcquisition
                            (c.getAcquisition().getId().toString(), Numbers.TWO.getNumber());
                    HashMap<String, List<ErrorField>> error = new HashMap<>();
                    error.put(ConstantsErrors.ERROR_CODE_COUNTRY_NOT_AVIABLE, Collections.singletonList
                            (ErrorField.builder().build()));
                    adapter.info(ERROR_CONTROLADO_APP);
                    throw new ValidationException(error);
                }
            });
        }
    }

    @Override
    public void maxRepetitionEmailCellphone(ContactInformation contactInformation) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        String email = contactInformation.getEmail();
        String cellphone = contactInformation.getCellphone();
        UUID acquisitionId = contactInformation.getAcquisition().getId();
        if (email != null) {
            List<ErrorField> errorFields = validationRepetitionMaxEmail(email,acquisitionId);
            if (!errorFields.isEmpty()) {
                error.put(ConstantsErrors.ERROR_CODE_EMAIL_REGISTRY, errorFields);
            }
        }
        if (cellphone != null) {
            List<ErrorField> errorFields = validationRepetitionMaxCellphone(cellphone,acquisitionId);
            if (!errorFields.isEmpty()) {
                error.put(ConstantsErrors.ERROR_CODE_CELLPHONE_REGISTRY, errorFields);
            }
        }
        if (!error.isEmpty()) {
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
    }

    @Override
    public boolean existsTypeAddressResBrand(
            Acquisition acquisition, List<ContactInformation> contactInformationList, List<ContactInformation> ciList) {
        boolean flg = contactInformationList.stream().filter(item -> item.getAddressType().equals(CO_ADDRESS_TYPE_RES))
                .count() > 0;
        List<ContactInformation> ciFilterZ001List = ciList.stream()
                .filter(predicate -> predicate.getAddressType().equals(CO_ADDRESS_TYPE_RES)
                        && !validateFieldsContactInfoNull(predicate))
                .collect(Collectors.toList());
        validateBrand(contactInformationList, ciFilterZ001List);
        if (flg) {
            return flg;
        } else {
            return (!ciFilterZ001List.isEmpty());
        }
    }

    @Override
    public void validateAddressType(List<ContactInformation> contactInformationList) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (contactInformationList.stream().collect(Collectors.groupingBy(ContactInformation::getAddressType))
                .entrySet().stream().anyMatch(e -> e.getValue().size() > 1)) {
            error.put(ConstantsErrors.ERROR_CODE_MANY_ADDRESS_TYPE, Collections.singletonList(ErrorField.builder()
                    .build()));
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
        contactInformationList.forEach(c -> {
            if (c.getAddressType().equals(CO_ADDRESS_TYPE_RES)) {
                this.maxRepetitionEmailCellphone(c);
                if (!c.getBrand().equals(CO_BRAND_TYPE_RES)) {
                    c.setBrand(CO_BRAND_TYPE_RES);
                }
            }
        });
    }

    public void validateBrand(
            List<ContactInformation> contactInformationList, List<ContactInformation> ciFilterZ001List) {
        Long lCountBrand = contactInformationList.stream().filter(c -> c.getBrand().equals(CO_BRAND_TYPE_RES)).count();
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (lCountBrand >= Numbers.TWO.getIntNumber()) {
            error.put(ConstantsErrors.ERROR_CODE_MATCH_MARK,
                    Collections.singletonList(ErrorField.builder().name(ConstantsErrors.ERROR_MSG_MULTIPLY_ADDRESS)
                            .build()));
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        } else if (lCountBrand == Numbers.ZERO.getIntNumber() && ciFilterZ001List.isEmpty()) {
            error.put(ConstantsErrors.ERROR_CODE_MATCH_MARK,
                    Collections.singletonList(ErrorField.builder().name(ConstantsErrors.ERROR_MSG_EMPTY_ADDRESS)
                            .build()));
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
    }

    public List<ErrorField> validationRepetitionMaxEmail(String email, UUID acquisitionId){
        List<ErrorField> errorFields =  new ArrayList<>();
        if (email != null) {
            List<UUID> acquisitionIdList = contactInformationRepository.findAcquisitionListByEmail(
                    email, acquisitionId, CO_ADDRESS_TYPE_RES);

            String emailParameterValue = Numbers.THREE.getNumber();
            Optional<Parameters> parameter=parametersUseCase.findByNameAndParent(EMAIL, PARAMETER_EMAIL);
            if(parameter.isPresent()){
                emailParameterValue=parameter.get().getCode();
            }

            if(acquisitionIdList.size()>=Long.parseLong(emailParameterValue)){
                Long result = vinculationUpdateUseCase.countAcquisitionByState(
                        co.com.bancolombia.usecase.util.constants.Constants.STATE_ACQ_ACTIVE ,acquisitionIdList);
                adapter.info(co.com.bancolombia.usecase.util.constants.Constants.COUNT_ACQ_STATE_EMAIL + result);
                errorFields = this.parametersUseCase.validationMaxRepeat(result, PARAMETER_EMAIL);
            }
        }
        return errorFields;
    }

    public List<ErrorField> validationRepetitionMaxCellphone(String cellphone, UUID acquisition){
        List<ErrorField> errorFields =  new ArrayList<>();
        if (cellphone != null) {
            List<UUID> acquisitionIdList = contactInformationRepository.findAcquisitionListByCellphone(
                    cellphone, acquisition, CO_ADDRESS_TYPE_RES);

            String cellphoneParameterValue = Numbers.THREE.getNumber();
            Optional<Parameters> parameter=parametersUseCase.findByNameAndParent(CELLPHONE, PARAMETER_CELLPHONE);
            if(parameter.isPresent()){
                cellphoneParameterValue=parameter.get().getCode();
            }

            if(acquisitionIdList.size()>=Long.parseLong(cellphoneParameterValue)){
                Long result = vinculationUpdateUseCase.countAcquisitionByState(
                        co.com.bancolombia.usecase.util.constants.Constants.STATE_ACQ_ACTIVE ,acquisitionIdList);
                adapter.info(co.com.bancolombia.usecase.util.constants.Constants.COUNT_ACQ_STATE_CELLPHONE + result);
                errorFields = this.parametersUseCase.validationMaxRepeat(result, PARAMETER_CELLPHONE);
            }
        }
        return errorFields;
    }
}