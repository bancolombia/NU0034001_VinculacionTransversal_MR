package co.com.bancolombia.usecase.foreigninformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import co.com.bancolombia.model.foreigninformation.gateways.ForeignInformationRepository;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.foreigninformationcurrency.ForeignInformationCurrencyUseCaseImpl;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_PENDIENTE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_FOREIGN_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_MANDATORY;

public class ForeignInformationUseCaseImpl
        extends ValidateInfoGeneric<ForeignInformation, ExecFieldReply, DependentFieldUseCase>
        implements ForeignInformationUseCase {

    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_FOREIGN_INF);

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateCatalogsForeignUseCase validateCatalogsForeignUseCase;
    private final MergeUseCase mergeUseCase;
    private final ForeignInformationCurrencyUseCaseImpl currencyUseCase;
    private final ForeignInformationRepository repository;

    public ForeignInformationUseCaseImpl(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsForeignUseCase validateCatalogsForeignUseCase, MergeUseCase mergeUseCase,
            ForeignInformationCurrencyUseCaseImpl currencyUseCase, ForeignInformationRepository repository) {
        super(dependentFieldUseCase);
        this.vinculationUpdateUseCase = vinculationUpdateUseCase;
        this.validateCatalogsForeignUseCase = validateCatalogsForeignUseCase;
        this.mergeUseCase = mergeUseCase;
        this.currencyUseCase = currencyUseCase;
        this.repository = repository;
    }

    @Override
    public ForeignInformation findByAcquisition(Acquisition acquisition){
        return repository.findByAcquisition(acquisition);
    }

    public ForeignInformationOperation startProcessForeignInformation(ForeignInformationOperation operation) {
        adapter.info(START_OPERATION);
        Acquisition acquisition = operation.getForeignInformation().getAcquisition();
        boolean bValidExists = false;
        List<ForeignInformationCurrency> listNotNull = firstStep(operation);
        ForeignInformation fI = repository.findByAcquisition(acquisition);
        if (fI != null) {
            mergeForeignInformation(fI, operation.getForeignInformation());
            fI.setAcquisition(acquisition);
            bValidExists = true;
        } else {
            fI = operation.getForeignInformation();
        }
        List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                .checkListStatus(fI.getAcquisition().getId(), CODE_FOREIGN_INFORMATION).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());
        fI.setForeignCurrencyList(listNotNull.isEmpty() ? null : listNotNull);
        mandatoryForeignInformation(fI, mandatoryExecFList);

        List<ExecFieldReply> mandatoryExecFList2 = vinculationUpdateUseCase
                .checkListStatus(fI.getAcquisition().getId(), CODE_FOREIGN_CURRENCY).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());
        List<ForeignInformationCurrency> list = currencyUseCase.mandatoryMergeForeignInfo(
                fI, listNotNull, mandatoryExecFList2, bValidExists);
        adapter.info(FINISH_OPERATION);
        return this.save(fI, list);
    }

    public void mergeForeignInformation(ForeignInformation oldFI, ForeignInformation newFI) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        MergeAttrib attrib = MergeAttrib.builder().nameList(null).stepCode(CODE_FOREIGN_INFORMATION)
                .isRecordUpgradeable(false).build();
        List<ErrorField> udpErrorField = mergeUseCase.merge(oldFI, newFI, attrib);
        if (!udpErrorField.isEmpty()) {
            adapter.error(ERROR_CODE_NON_MODIFIABLE + udpErrorField);
            error.put(ERROR_CODE_NON_MODIFIABLE, udpErrorField);
            throw new ValidationException(error);
        }
        oldFI.setUpdatedBy(newFI.getCreatedBy());
        oldFI.setUpdatedDate(newFI.getCreatedDate());
    }

    public void mandatoryForeignInformation(ForeignInformation info, List<ExecFieldReply> mandatoryExecFList) {
        List<ErrorField> errorFields = validateMandatory(info, mandatoryExecFList,
                info.getForeignCurrencyTransaction(), DependentFieldParamValidator.builder()
                        .acquisition(info.getAcquisition()).operation(OPER_FOREIGN_INF).build());
        if (errorFields != null && !errorFields.isEmpty()) {
            adapter.error(ERROR_CODE_OPTIONAL_MANDATORY + errorFields);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_OPTIONAL_MANDATORY, errorFields);
            throw new ValidationException(error);
        }
    }

    public ForeignInformationOperation save(ForeignInformation information, List<ForeignInformationCurrency> list) {
        ForeignInformation fiSaved = this.repository.save(information);
        currencyUseCase.save(fiSaved, list);
        vinculationUpdateUseCase.markOperation(
                fiSaved.getAcquisition().getId(), CODE_FOREIGN_INFORMATION, CODE_ST_OPE_COMPLETADO);
        vinculationUpdateUseCase.markOperation(
                fiSaved.getAcquisition().getId(), CODE_FOREIGN_CURRENCY, CODE_ST_OPE_COMPLETADO);
        return ForeignInformationOperation.builder().foreignInformation(fiSaved).list(list).build();
    }

    public List<ForeignInformationCurrency> firstStep(ForeignInformationOperation operation) {
        ForeignInformation fi = repository.findByAcquisition(operation.getForeignInformation().getAcquisition());
        List<ForeignInformationCurrency> listNotNull = currencyUseCase.validateListNull(operation, fi);
        validateCatalogsForeignUseCase.validateForeignInformation(operation.getForeignInformation());
        validateCatalogsForeignUseCase.validateForeignIInformationCurrency(operation.getList(),
                operation.getForeignInformation().getForeignCurrencyTransaction());
        vinculationUpdateUseCase.markOperation(
                operation.getForeignInformation().getAcquisition().getId(),
                CODE_FOREIGN_INFORMATION, CODE_ST_OPE_PENDIENTE);
        vinculationUpdateUseCase.markOperation(
                operation.getForeignInformation().getAcquisition().getId(),
                CODE_FOREIGN_CURRENCY, CODE_ST_OPE_PENDIENTE);
        return listNotNull;
    }
}