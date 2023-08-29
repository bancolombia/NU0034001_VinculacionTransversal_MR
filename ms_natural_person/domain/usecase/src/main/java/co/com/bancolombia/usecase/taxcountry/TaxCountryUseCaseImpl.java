package co.com.bancolombia.usecase.taxcountry;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.model.taxcountry.gateways.TaxCountryRepository;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_TAX_COUNTRY_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.POSITION_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TAX_COUNTRY_IDENTIFIER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_EQUAL_IDENTIFIER_LIST_TAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FIELD_MANDATORY_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE_LIST;

public class TaxCountryUseCaseImpl extends
        ValidateInfoGeneric<TaxCountry, ExecFieldReply, DependentFieldUseCase>
        implements TaxCountryUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final TaxCountryRepository taxCountryRepository;
    private final MergeUseCase mergeUseCase;
    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_TAX_COUNTRY_INF);

    public TaxCountryUseCaseImpl(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            TaxCountryRepository taxCountryRepository, MergeUseCase mergeUseCase) {
        super(dependentFieldUseCase);
        this.vinculationUpdateUseCase = vinculationUpdateUseCase;
        this.taxCountryRepository = taxCountryRepository;
        this.mergeUseCase = mergeUseCase;
    }

    @Override
    public List<TaxCountry> findAllByAcquisition(Acquisition acquisition){
        return taxCountryRepository.findAllByAcquisition(acquisition);
    }

    @Override
    public List<TaxCountry> saveAll(List<TaxCountry> taxCountryList) {
        return taxCountryRepository.saveAll(taxCountryList);
    }

    public List<TaxCountry> valMandatoryFieldsAndMerge(
            Acquisition acq, List<TaxCountry> taxCountryList, Object dependentObject) {
        List<ErrorField> errorFieldsMandatory = new ArrayList<>();
        List<ErrorField> errorFieldsMerge = new ArrayList<>();
        List<TaxCountry> tInfoUpd = new ArrayList<>();
        List<TaxCountry> tcListFounded = taxCountryRepository.findAllByAcquisition(acq);
        if (!tcListFounded.isEmpty()) {
            List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                    .checkListStatus(acq.getId(), CODE_TAX_COUNTRY).getExecFieldList()
                    .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());
            List<TaxCountry> finalTInfoUpd = tInfoUpd;
            List<ErrorField> finalErrorFieldsMandatory = errorFieldsMandatory;
            taxCountryList.forEach(tcNew -> { Optional<TaxCountry> tcOld = filterTaxCountry(tcListFounded, tcNew);
                if (tcOld.isPresent()) {
                    List<ErrorField> errorMerge = mergeFieldsTaxCountry(tcOld, tcNew);
                    if (!errorMerge.isEmpty()) {
                        errorFieldsMerge.addAll(errorMerge);
                    } else { tcOld = Optional.ofNullable(tcOld.get().toBuilder()
                            .updatedBy(tcNew.getCreatedBy()).updatedDate(tcNew.getCreatedDate()).build());
                        finalTInfoUpd.add(tcOld.get()); }} else { finalTInfoUpd.add(tcNew);
                    finalErrorFieldsMandatory.addAll(this.validateMandatory(tcNew,mandatoryExecFList,
                            EMPTY + tcNew.getIdentifier(), DependentFieldParamValidator.builder().acquisition(acq)
                            .operation(OPER_TAX_COUNTRY_INF).dependentObject(dependentObject).build()));}
            }); validateIfErrorField(finalErrorFieldsMandatory, errorFieldsMerge);
        } else { tInfoUpd = taxCountryList; }
        if (tcListFounded.isEmpty()) {
            errorFieldsMandatory = mandatoryFieldsTaxCountry(acq, taxCountryList, dependentObject);
            validateIfErrorField(errorFieldsMandatory, errorFieldsMerge);}
        vinculationUpdateUseCase.markOperation(acq.getId(), CODE_TAX_COUNTRY, CODE_ST_OPE_COMPLETADO);
        return tInfoUpd;
    }

    public List<ErrorField> mergeFieldsTaxCountry(Optional<TaxCountry> taxCountryOld, TaxCountry taxCountryNew) {
        assert taxCountryOld.isPresent();
        MergeAttrib mergeAttrib = MergeAttrib.builder().stepCode(CODE_TAX_COUNTRY)
                .nameList(POSITION_LIST + taxCountryOld.get().getIdentifier()).isRecordUpgradeable(false).build();
        return mergeUseCase.merge(taxCountryOld.get(), taxCountryNew, mergeAttrib);
    }

    public List<ErrorField> mandatoryFieldsTaxCountry(Acquisition acquisition, List<TaxCountry> taxCountryList,
            Object dependentObject) {
        List<ErrorField> errorFieldsMandatory = new ArrayList<>();
        List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                .checkListStatus(acquisition.getId(), CODE_TAX_COUNTRY).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());
        taxCountryList.forEach(item -> errorFieldsMandatory.addAll(validateMandatory(item, mandatoryExecFList,
                EMPTY + item.getIdentifier(), DependentFieldParamValidator.builder().acquisition(acquisition)
                        .operation(OPER_TAX_COUNTRY_INF).dependentObject(dependentObject).build())));
        return errorFieldsMandatory;
    }

    public void validateIfErrorField(List<ErrorField> efMandatory, List<ErrorField> efMerge) {
        if (!efMandatory.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_FIELD_MANDATORY_LIST, efMandatory);
            adapter.error(ERROR_CODE_FIELD_MANDATORY_LIST+efMandatory);
            throw new ValidationException(error);
        }

        if (!efMerge.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_NON_MODIFIABLE_LIST, efMerge);
            adapter.error(ERROR_CODE_NON_MODIFIABLE_LIST+efMerge);
            throw new ValidationException(error);
        }
    }

    public Optional<TaxCountry> filterTaxCountry(List<TaxCountry> taxCountryListSaved, TaxCountry taxCountryReceived) {
        return taxCountryListSaved.stream()
                .filter(predicate -> predicate.getIdentifier().equals(taxCountryReceived.getIdentifier())).findFirst();
    }

    public List<TaxCountry> validateIdentifier(List<TaxCountry> taxCountryList) {
        List<ErrorField> errorQuality = new ArrayList<>();

        for (int i = 0; i < taxCountryList.size() - 1; i++) {
            for (int j = i + 1; j <= taxCountryList.size() - 1; j++) {
                if (taxCountryList.get(i).getIdentifier().equals(taxCountryList.get(j).getIdentifier())) {
                    int position = j + 1;
                    ErrorField errorField = ErrorField.builder().name(taxCountryList.get(i).getIdentifier() + "")
                            .complement(TAX_COUNTRY_IDENTIFIER).nameList(EMPTY + position).build();
                    errorQuality.add(errorField);
                    break;
                }
            }
        }

        if (!errorQuality.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_EQUAL_IDENTIFIER_LIST_TAX, errorQuality);
            adapter.error(ERROR_CODE_EQUAL_IDENTIFIER_LIST_TAX+errorQuality);
            throw new ValidationException(error);
        }
        return taxCountryList;
    }
}