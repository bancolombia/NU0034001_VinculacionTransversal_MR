package co.com.bancolombia.usecase.personalinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCase;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.ValidateMandatoryFields;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CO_ADDRESS_TYPE_RES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_CELLPHONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_EMAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CELLPHONE_REGISTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_EMAIL_REGISTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_MANDATORY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers.THREE;
import static co.com.bancolombia.usecase.util.constants.Constants.STATE_ACQ_ACTIVE;


@RequiredArgsConstructor
public class PersonalInformationUseCaseImpl implements PersonalInformationUseCase {

    private final PersonalInformationRepository pInfoRepository;
    private final ContactInformationUseCase contactInformationUseCase;
    private final ParametersUseCase parametersUseCase;
    private final ValidateMandatoryFields validateMandatoryFields;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateCatalogsPersonalUseCase validateCatalogsPersonalUseCase;

    LoggerAdapter adapter = new LoggerAdapter(Constants.PRODUCT_VTN,
            Constants.CODE_PERSONAL_INFO, this.getClass().getName());

    @Override
    public HashMap<String, List<ErrorField>> validationRepetitionMaxEmail(String email, UUID acquisitionId) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (email != null) {
            List<UUID> acquisitionIdList = pInfoRepository.findAcquisitionListByEmail(email, acquisitionId);

            String emailParameterValue = THREE.getNumber();
            Optional<Parameters> parameter=parametersUseCase.findByNameAndParent(EMAIL, PARAMETER_EMAIL);
            if(parameter.isPresent()){
                emailParameterValue=parameter.get().getCode();
            }

            List<ErrorField> errorFields =  new ArrayList<>();
            if(acquisitionIdList.size()>=Long.parseLong(emailParameterValue)){
                Long result = vinculationUpdateUseCase.countAcquisitionByState(STATE_ACQ_ACTIVE ,acquisitionIdList);
                adapter.info(co.com.bancolombia.usecase.util.constants.Constants.COUNT_ACQ_STATE_EMAIL + result);
                errorFields = this.parametersUseCase.validationMaxRepeat(result, PARAMETER_EMAIL);
            }
            if (!errorFields.isEmpty()) {
                error.put(ERROR_CODE_EMAIL_REGISTRY, errorFields);
            }
        }
        return error;
    }

    @Override
    public HashMap<String, List<ErrorField>> validationRepetitionMaxCellphone(
            String cellphone, UUID acquisition) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        if (cellphone != null) {
            List<UUID> acquisitionIdList = pInfoRepository.findAcquisitionListByCellphone(cellphone, acquisition);

            String cellphoneParameterValue = THREE.getNumber();
            Optional<Parameters> parameter=parametersUseCase.findByNameAndParent(CELLPHONE, PARAMETER_CELLPHONE);
            if(parameter.isPresent()){
                cellphoneParameterValue=parameter.get().getCode();
            }

            List<ErrorField> errorFields =  new ArrayList<>();
            if(acquisitionIdList.size()>=Long.parseLong(cellphoneParameterValue)){
                Long result = vinculationUpdateUseCase.countAcquisitionByState(STATE_ACQ_ACTIVE ,acquisitionIdList);
                adapter.info(co.com.bancolombia.usecase.util.constants.Constants.COUNT_ACQ_STATE_CELLPHONE + result);
                errorFields = this.parametersUseCase.validationMaxRepeat(result, PARAMETER_CELLPHONE);
            }

            if (!errorFields.isEmpty()) {
                error.put(ERROR_CODE_CELLPHONE_REGISTRY, errorFields);
            }
        }
        return error;
    }

    @Override
    public PersonalInformation save(
            PersonalInformation pInfo, PersonalInformation info) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();

        List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                .checkListStatus(info.getAcquisition().getId(), CODE_PERSONAL_INFO).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());

        this.validateMandatoryFields(info,mandatoryExecFList);
        Acquisition acquisition = pInfo.getAcquisition();
        error.putAll(this.validationRepetitionMaxEmail(pInfo.getEmail(), acquisition.getId()));
        error.putAll(this.validationRepetitionMaxCellphone(pInfo.getCellphone(), acquisition.getId()));
        if (!error.isEmpty()) {
            adapter.error(ERROR+error);
            throw new ValidationException(error);
        }
        this.validateCatalogsPersonalUseCase.validatePersonalInfoCatalogs(pInfo);
        this.contactInformationUseCase.save(
                ContactInformation.builder()
                        .cellphone(pInfo.getCellphone()).email(pInfo.getEmail()).addressType(CO_ADDRESS_TYPE_RES)
                        .acquisition(pInfo.getAcquisition()).createdBy(pInfo.getCreatedBy())
                        .createdDate(pInfo.getCreatedDate()).build());

        vinculationUpdateUseCase.markOperation(
                pInfo.getAcquisition().getId(), CODE_PERSONAL_INFO, CODE_ST_OPE_COMPLETADO);
        adapter.info(FINISH_OPERATION);
        return this.pInfoRepository.save(info);
    }

    public void validateMandatoryFields(PersonalInformation info, List<ExecFieldReply> mandatoryExecFList) {
        List<ErrorField> errorFields = validateMandatoryFields.validateMandatoryFields(info, mandatoryExecFList);
        if (errorFields != null && !errorFields.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_OPTIONAL_MANDATORY, errorFields);
            adapter.error(ERROR+ERROR_CODE_OPTIONAL_MANDATORY+MIDDLE_SCREEN+errorFields);
            throw new ValidationException(error);
        }
    }

    public Optional<PersonalInformation> findByAcquisition(Acquisition acquisition) {
        return Optional.ofNullable(pInfoRepository.findByAcquisition(acquisition));
    }

    @Override
    public PersonalInformation save(PersonalInformation personalInformation){
        return pInfoRepository.save(personalInformation);
    }
}