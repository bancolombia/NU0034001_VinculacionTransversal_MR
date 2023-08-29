package co.com.bancolombia.usecase.taxinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.taxinformation.TaxInformation;
import co.com.bancolombia.taxinformation.gateways.TaxInformationRepository;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.taxcountry.TaxCountryUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_TAX_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_INCONSISTENT_RECORDS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE;

public class TaxInformationUseCaseImpl extends
        ValidateInfoGeneric<TaxInformation, ExecFieldReply, DependentFieldUseCase> implements TaxInformationUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateCatalogsTaxUseCase validateCatalogsTaxUseCase;
    private final TaxInformationRepository taxInformationRepository;
    private final TaxCountryUseCase taxCountryUseCase;
    private final MergeUseCase mergeUseCase;
    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_TAX_INF);

    public TaxInformation findByAcquisition(Acquisition acquisition){
        return taxInformationRepository.findByAcquisition(acquisition);
    }

    public TaxInformationUseCaseImpl(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsTaxUseCase validateCatalogsTaxUseCase, TaxInformationRepository taxInformationRepository,
            TaxCountryUseCase taxCountryUseCase, MergeUseCase mergeUseCase) {
        super(dependentFieldUseCase);
        this.vinculationUpdateUseCase = vinculationUpdateUseCase;
        this.validateCatalogsTaxUseCase = validateCatalogsTaxUseCase;
        this.taxInformationRepository = taxInformationRepository;
        this.taxCountryUseCase = taxCountryUseCase;
        this.mergeUseCase = mergeUseCase;
    }

    @Override
    public TaxInformation startProcessTaxInformation(TaxInformation taxInformation, List<TaxCountry> taxCountryList) {
        adapter.info(START_OPERATION);
        validateCountries(taxInformation, taxCountryList);
        TaxInformation info = this.taxInformationRepository.findByAcquisition(taxInformation.getAcquisition());
        if (info != null) {
            mergeFieldsTaxInfo(info, taxInformation);
            info.setUpdatedBy(taxInformation.getCreatedBy());
            info.setUpdatedDate(taxInformation.getCreatedDate());
            info.setAcquisition(taxInformation.getAcquisition());
        } else { info = taxInformation; }
        info.setCountryList(taxCountryList.isEmpty() ? null : taxCountryList);
        mandatoryFieldsTaxInfo(info);
        List<TaxCountry> txCountVal = new ArrayList<>();
        if (!taxCountryList.isEmpty()) {
            txCountVal = taxCountryUseCase.valMandatoryFieldsAndMerge(info.getAcquisition(), taxCountryList, info);
            taxCountryUseCase.validateIdentifier(txCountVal);
        }
        TaxInformation finalTaxInformation = taxInformationRepository.save(info);
        if (!txCountVal.isEmpty()) {
            txCountVal.forEach(taxCountry -> taxCountry.setTaxInformation(finalTaxInformation));
            taxCountryUseCase.saveAll(txCountVal);
        } else {
            taxCountryList.forEach(taxCountry -> taxCountry.setTaxInformation(finalTaxInformation));
            taxCountryUseCase.saveAll(taxCountryList);
        }
        vinculationUpdateUseCase.markOperation(info.getAcquisition().getId(), CODE_TAX_INFO, CODE_ST_OPE_COMPLETADO);
        adapter.info(FINISH_OPERATION);
        return info;
    }

    private void validateCountries(TaxInformation taxInformation, List<TaxCountry> taxCountryList) {
        validateCatalogsTaxUseCase.validateTaxInfoCountryCatalogs(taxInformation, taxCountryList);
        validateRepeatedCountries(taxInformation.getCountry(), taxCountryList);
    }

    public void mandatoryFieldsTaxInfo(TaxInformation taxInformation) {
        List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                .checkListStatus(taxInformation.getAcquisition().getId(), CODE_TAX_INFO).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());
        this.validateMandatoryFields(
                taxInformation, mandatoryExecFList, "", DependentFieldParamValidator.builder()
                        .acquisition(taxInformation.getAcquisition()).operation(OPER_TAX_INF).build());
    }

    public void mergeFieldsTaxInfo(TaxInformation infoSavedInDb, TaxInformation taxInformationNew) {
        MergeAttrib mergeAttrib = MergeAttrib.builder().stepCode(CODE_TAX_INFO).nameList(null)
                .isRecordUpgradeable(false).build();
        List<ErrorField> upErrField = this.mergeUseCase.merge(infoSavedInDb, taxInformationNew, mergeAttrib);
        if (!upErrField.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_NON_MODIFIABLE, upErrField);
            adapter.error(ERROR_CODE_NON_MODIFIABLE+upErrField);
            throw new ValidationException(error);
        }
    }

    public void validateRepeatedCountries(String country, List<TaxCountry> taxCountryList) {
        List<String> countries = new ArrayList<>();
        countries.add(country);
        boolean repeated = false;
        for (TaxCountry taxCountry : taxCountryList) {
            if (countries.contains(taxCountry.getCountry())) {
                repeated = true;
                break;
            }
            else {
                countries.add(taxCountry.getCountry());
            }
        }
        if (repeated) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            List<ErrorField> eFieldList = Collections.singletonList(ErrorField.builder().build());
            error.put(ERROR_CODE_INCONSISTENT_RECORDS, eFieldList);
            adapter.error(ERROR_CODE_INCONSISTENT_RECORDS);
            throw new ValidationException(error);
        }
    }
}