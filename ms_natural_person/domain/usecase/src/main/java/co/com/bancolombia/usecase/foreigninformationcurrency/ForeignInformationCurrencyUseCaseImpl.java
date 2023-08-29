package co.com.bancolombia.usecase.foreigninformationcurrency;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.model.foreigninformationcurrency.gateways.ForeignInformationCurrencyRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FOREIGN_INFORMATION_OP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CURRENCY_TRANSACTION_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INCONSISTENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOTHING_PRODUCT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.N_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_FOREIGN_CURR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_FOREIGN_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.POSITION_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_INFORMATION_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE_LIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_LIST_MANDATORY;

public class ForeignInformationCurrencyUseCaseImpl
        extends ValidateInfoGeneric<ForeignInformationCurrency, ExecFieldReply, DependentFieldUseCase>
        implements ForeignInformationCurrencyUseCase {

    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_FOREIGN_INF);

    private final MergeUseCase mergeUseCase;
    private final ForeignInformationCurrencyRepository repository;

    public ForeignInformationCurrencyUseCaseImpl(
            DependentFieldUseCase dependentFieldUseCase, MergeUseCase mergeUseCase,
            ForeignInformationCurrencyRepository repository) {
        super(dependentFieldUseCase);
        this.mergeUseCase = mergeUseCase;
        this.repository = repository;
    }

    public List<ForeignInformationCurrency> findByForeignInformation(ForeignInformation foreignInformation){
        return repository.findByForeignInformation(foreignInformation);
    }

    public List<ForeignInformationCurrency> mandatoryMergeForeignInfo(
            ForeignInformation fI, List<ForeignInformationCurrency> list,
            List<ExecFieldReply> mandatoryExecFList, boolean bValidExists) {
        List<ForeignInformationCurrency> old = new ArrayList<>();
        if (bValidExists) {
            old = repository.findByForeignInformation(fI);
        }
        if (!old.isEmpty()) {
            list = mergeForeignInformationCurrency(list, old);
        }
        this.mandatoryForeignInformationCurrency(list, mandatoryExecFList, fI);
        return list;
    }

    public List<ForeignInformationCurrency> mergeForeignInformationCurrency(
            List<ForeignInformationCurrency> list, List<ForeignInformationCurrency> old) {
        HashMap<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> udpErrorField = new ArrayList<>();
        List<ForeignInformationCurrency> data = new ArrayList<>();
        list.forEach(curr -> {
            Optional<ForeignInformationCurrency> oldData = old.stream()
                    .filter(o -> o.getForeignCurrencyTransactionType().equals(curr.getForeignCurrencyTransactionType()))
                    .findFirst();
            if (oldData.isPresent()) {
                MergeAttrib attrib = MergeAttrib.builder().nameList(POSITION_LIST.concat(oldData.get()
                        .getForeignCurrencyTransactionType())).stepCode(CODE_FOREIGN_CURRENCY)
                        .isRecordUpgradeable(false).build();
                udpErrorField.addAll(mergeUseCase.merge(oldData.get(), curr, attrib));
                oldData.get().setUpdatedBy(curr.getCreatedBy());
                oldData.get().setUpdatedDate(curr.getCreatedDate());
                data.add(oldData.get());
            } else {
                data.add(curr);
            }
        });
        if (!udpErrorField.isEmpty()) {
            adapter.error(ERROR_CODE_NON_MODIFIABLE_LIST + udpErrorField);
            error.put(ERROR_CODE_NON_MODIFIABLE_LIST, udpErrorField);
            throw new ValidationException(error);
        }
        return data;
    }

    public void mandatoryForeignInformationCurrency(
            List<ForeignInformationCurrency> list, List<ExecFieldReply> mandatoryExecFList, ForeignInformation fI) {
        List<ErrorField> errorFields = new ArrayList<>();
        list.forEach(item -> errorFields.addAll(validateMandatory(item, mandatoryExecFList,
                item.getForeignCurrencyTransactionType(), DependentFieldParamValidator.builder()
                        .acquisition(fI.getAcquisition()).operation(OPER_FOREIGN_CURR).dependentObject(fI).build())));
        if (!errorFields.isEmpty()) {
            adapter.error(ERROR_CODE_OPTIONAL_LIST_MANDATORY + errorFields);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_OPTIONAL_LIST_MANDATORY, errorFields);
            throw new ValidationException(error);
        }
    }

    public List<ForeignInformationCurrency> save(ForeignInformation fI, List<ForeignInformationCurrency> list) {
        list.forEach(item -> item.setForeignInformation(fI));
        return this.repository.saveAll(list);
    }

    public List<ErrorField> mergeFieldsForeingInfo(
            ForeignInformationCurrency infoSavedInDb, ForeignInformationCurrency contInformationNew) {
        MergeAttrib mergeAttrib = MergeAttrib.builder().stepCode(CODE_FOREIGN_CURRENCY).nameList(POSITION_LIST
                .concat(infoSavedInDb.getForeignCurrencyTransactionType())).isRecordUpgradeable(false).build();
        return this.mergeUseCase.merge(infoSavedInDb, contInformationNew, mergeAttrib);
    }

    public List<ForeignInformationCurrency> updateInfoObjectNew(
            List<ForeignInformationCurrency> cInfo, ForeignInformation fi) {
        List<ForeignInformationCurrency> cInfoFinal = new ArrayList<>();
        List<ErrorField> upErrField = new ArrayList<>();
        if (cInfo != null) {
            cInfo.forEach(infoItem -> {
                if (fi != null) {
                    ForeignInformationCurrency infoBd = repository
                            .findByForeignInformationAndForeignCurrencyTransactionType(
                                    fi, infoItem.getForeignCurrencyTransactionType()).orElse(null);
                    if (infoBd != null) {
                        upErrField.addAll(mergeFieldsForeingInfo(infoBd, infoItem));
                    } else {
                        infoBd = infoItem;
                    }
                    cInfoFinal.add(infoBd);
                } else {
                    cInfoFinal.add(infoItem);
                }
            });
        }
        validateErrorMerge(upErrField);
        return cInfoFinal;
    }

    public void validateErrorMerge(List<ErrorField> upErrField) {
        if (!upErrField.isEmpty()) {
            adapter.error(ERROR_CODE_NON_MODIFIABLE_LIST + upErrField);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_NON_MODIFIABLE_LIST, upErrField);
            throw new ValidationException(error);
        }
    }

    public List<ForeignInformationCurrency> validateListNull(
            ForeignInformationOperation operation, ForeignInformation fi) {
        List<ForeignInformationCurrency> lFiC = updateInfoObjectNew(operation.getList(), fi);
        if (operation.getForeignInformation().getForeignCurrencyTransaction()
                .equals(N_FOREIGN_INFORMATION) && (lFiC == null || lFiC.isEmpty())) {
            return Collections.singletonList(
                    ForeignInformationCurrency.builder().foreignCurrencyTransactionType(NOTHING_PRODUCT)
                            .createdDate(operation.getForeignInformation().getCreatedDate())
                            .createdBy(operation.getForeignInformation().getCreatedBy())
                            .foreignInformation(operation.getForeignInformation())
                            .acquisition(operation.getForeignInformation().getAcquisition()).build());
        }
        if (lFiC != null && !lFiC.isEmpty()) {
            List<PropertyDescriptor> props = mergeUseCase.getAnnotatedFieldsProperties(operation.getList().get(0));
            operation.getList().forEach(
                    inf -> validateList(operation.getForeignInformation().getForeignCurrencyTransaction(), inf, props));
        }
        return operation.getList();
    }

    public void validateList(String transaction, ForeignInformationCurrency info, List<PropertyDescriptor> props) {
        boolean flg = false;
        for (PropertyDescriptor curr : props) {
            try {
                Object data = curr.getReadMethod().invoke(info);
                if (!curr.getName().equals(F_CURRENCY_TRANSACTION_TYPE) && data != null) {
                    flg = true;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                adapter.error(Level.SEVERE+e.getMessage());
            }
        }
        boolean err = validateRule(flg, info.getForeignCurrencyTransactionType(), transaction);
        if (err) {
            adapter.error(ERROR_CODE_INFORMATION_OPERATION + INCONSISTENCY);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_INFORMATION_OPERATION,
                    Collections.singletonList(ErrorField.builder().name(FOREIGN_INFORMATION_OP).build()));
            throw new ValidationException(error);
        }
    }

    public boolean validateRule(boolean flg, String transType, String transaction) {
        boolean lflgFirst = transType == null;
        boolean lflgSecond = (flg && NOTHING_PRODUCT.equals(transType)) ||
                (flg && N_FOREIGN_INFORMATION.equals(transaction));
        boolean lflgThird = N_FOREIGN_INFORMATION.equals(transaction) && !NOTHING_PRODUCT.equals(transType);
        boolean lflgFourth = !N_FOREIGN_INFORMATION.equals(transaction) && !flg && NOTHING_PRODUCT.equals(transType);
        return lflgFirst || lflgSecond || lflgThird || lflgFourth;
    }
}