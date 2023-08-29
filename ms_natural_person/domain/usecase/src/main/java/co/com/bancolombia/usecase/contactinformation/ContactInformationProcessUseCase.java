package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.contactinformation.generic.ArrayErrors;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTACT_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONCAT_TYPE_ADDRESS_F;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CONCAT_TYPE_ADDRESS_I;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_CONTROLADO_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_ADDRESS_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_CONTACT_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_MAIN_ADDRESS;

public class ContactInformationProcessUseCase
        extends ValidateInfoGeneric<ContactInformation, ExecFieldReply, DependentFieldUseCase> {

    private final ContactInformationUseCasePersist cInfoUCPer;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateCatalogsContactUseCase validateCatalogsContactUseCase;

    public ContactInformationProcessUseCase(
            DependentFieldUseCase dependentFieldUseCase, ContactInformationUseCasePersist cInfoUCPer,
            VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsContactUseCase validateCatalogsContactUseCase) {
        super(dependentFieldUseCase);
        this.cInfoUCPer = cInfoUCPer;
        this.vinculationUpdateUseCase = vinculationUpdateUseCase;
        this.validateCatalogsContactUseCase = validateCatalogsContactUseCase;
    }

    LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_IDENTITY, OPER_CONTACT_INF);

    public String concatErrorTypeDirection(String tpDirection) {
        return CONCAT_TYPE_ADDRESS_I + tpDirection + CONCAT_TYPE_ADDRESS_F;
    }

    public List<ContactInformation> firstStepStartProcess(
            Acquisition a, List<ContactInformation> ciRe, ArrayErrors<ContactInformation> errorsList) {

        List<ErrorField> errorFieldsTypeDirection = new ArrayList<>();
        ciRe.stream().filter(predicate -> predicate.getAddressType() == null).forEach(item -> errorFieldsTypeDirection
                .add(ErrorField.builder().name(F_ADDRESS_TYPE).complement(EMPTY).build()));
        cInfoUCPer.getCValidationUseCase().validateIfErrorField(errorFieldsTypeDirection, errorsList.errorFieldsMerge);
        List<ErrorField> errorFields = new ArrayList<>();
        ciRe.forEach(item -> errorFields.addAll(validateMandatory(item, errorsList.mandatoryExecFList,
                concatErrorTypeDirection(item.getAddressType()),
                DependentFieldParamValidator.builder().acquisition(a).operation(OPER_CONTACT_INF).build())));

        cInfoUCPer.getCValidationUseCase().validateIfErrorField(errorFields, errorsList.errorFieldsMerge);
        cInfoUCPer.getCValidationUseCase().validateAddressType(ciRe);
        cInfoUCPer.getCValidationUseCase().validateForeignCountry(ciRe);
        validateCatalogsContactUseCase.validateContactInfoCatalogs(ciRe);
        List<ContactInformation> ciBd = cInfoUCPer.getContactInformationRepository().findAllByAcquisition(a);
        if (!cInfoUCPer.getCValidationUseCase().existsTypeAddressResBrand(a, ciRe, ciBd)) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_MAIN_ADDRESS, Collections.singletonList(ErrorField.builder().build()));
            adapter.info(ERROR_CONTROLADO_APP);
            throw new ValidationException(error);
        }
        vinculationUpdateUseCase.markOperation(
                a.getId(), CODE_CONTACT_INFO, CODE_ST_OPE_COMPLETADO);
        return ciBd;
    }
}
