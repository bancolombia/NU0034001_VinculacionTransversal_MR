package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.validateinfogeneric.ErrorFieldObject;
import co.com.bancolombia.common.validateinfogeneric.ObjectValidation;
import co.com.bancolombia.common.validateinfogeneric.ParamStreamDeclaratedField;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentArr;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.dependentfield.MyClassDependent;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_FIELD_DEPENDENT_OTHER_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_OPTIONAL_MANDATORY;

public class ValidateInfoGeneric<O, M extends ExecFieldReply, D extends DependentFieldUseCase> {
    private static final Logger LOGGER = Logger.getLogger(ValidateInfoGeneric.class.getName());

    private D dependentFieldUseCase;

    protected ValidateInfoGeneric(D dependentFieldUseCase) {
        this.dependentFieldUseCase = dependentFieldUseCase;
    }

    public void validateMandatoryFields(O info, List<M> mandatoryExecFList, String nameList,
            DependentFieldParamValidator depFieldParamVal) {
        String x = nameList != null ? nameList : "";
        List<ErrorField> errorFields = this.validateMandatory(info, mandatoryExecFList, x, depFieldParamVal);
        if (errorFields != null && !errorFields.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_OPTIONAL_MANDATORY, errorFields);
            throw new ValidationException(error);
        }
    }

    public List<ErrorField> validaIfGetValue(O info, List<M> mandatoryFieldList, ObjectValidation objectValidation) {
        List<ErrorField> retList = new ArrayList<>();
        String pName = objectValidation.aField.getName();
        Arrays.stream(objectValidation.props).filter(prop -> prop.getName().equals(objectValidation.aField.getName()))
                .findFirst().ifPresent(currProp -> {
                    try {
                        Object value = currProp.getReadMethod().invoke(info);
                        boolean any = mandatoryFieldList.stream().anyMatch(item -> item.getName().equals(pName));
                        boolean anyDependent = objectValidation.listDependenciaSame.stream()
                                .anyMatch(item -> item.getMyField().equals(pName) && item.isMandatory());
                        if (value == null && (any || anyDependent)) {
                            if (objectValidation.isOtherOperation) {
                                Optional<MyClassDependent> g = objectValidation.listDependenciaSame.stream()
                                        .filter(item -> item.getMyField().equals(pName)).findFirst();
                                if (g.isPresent()) {
                                    retList.addAll(validateDependency(objectValidation.listDependenciaSame,
                                            currProp, constructErrorField(objectValidation, g.get(), "")));
                                }
                            } else {
                                retList.addAll(validateDependency(objectValidation.listDependenciaSame, currProp,
                                        constructErrorField(objectValidation, null, currProp.getName())

                ));
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                });

        return retList;
    }

    public ErrorField constructErrorField(
            ObjectValidation objectValidation, MyClassDependent myClassDependent, String currPropName) {
        if (myClassDependent != null) {
            return ErrorField.builder().name(objectValidation.nameList)
                    .complement("0,".concat(myClassDependent.getCurrentOperation()).concat(",")
                            .concat(myClassDependent.getDependentOperation()).concat(",")
                            .concat(myClassDependent.getCurrentField()).concat(",")
                            .concat(myClassDependent.getDependentField()))
                    .build();
        } else {
            return ErrorField.builder().name(currPropName).complement(objectValidation.nameList).build();
        }
    }

    public List<ErrorField> validateMandatory(O info, List<M> mandatoryFieldList, String nameList,
            DependentFieldParamValidator depFieldParamVal) {
        DependentArr listDependencia = null;
        if (depFieldParamVal != null) {
            listDependencia = dependentFieldUseCase.principal(info, depFieldParamVal.getAcquisition(),
                    depFieldParamVal.getOperation(), depFieldParamVal.getDependentObject());
        }
        List<MyClassDependent> listDependenciaSame;
        List<MyClassDependent> listDependenciaOther;
        ErrorFieldObject errorFieldObject = null;
        if (listDependencia != null) {
            listDependenciaSame = listDependencia.getDependentSameOperatation();
            listDependenciaOther = listDependencia.getDependentOtherOperatation();
            try {
                PropertyDescriptor[] props = Introspector.getBeanInfo(info.getClass()).getPropertyDescriptors();
                errorFieldObject = streamDeclaratedField(info, mandatoryFieldList,
                        ParamStreamDeclaratedField.builder().nameList(nameList).props(props)
                                .depFieldParamVal(depFieldParamVal).listDependenciaSame(listDependenciaSame)
                                .listDependenciaOther(listDependenciaOther).build()
                );
            } catch (IntrospectionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            if (errorFieldObject != null && !errorFieldObject.retListOther.isEmpty()) {
                this.validateIsErrorDependentOtherOperation(errorFieldObject.retListOther);
            }
        }
        return (errorFieldObject != null && !errorFieldObject.retListSame.isEmpty()) ? errorFieldObject.retListSame
                : new ArrayList<>();
    }

    public ErrorFieldObject streamDeclaratedField(O info, List<M> mandatoryFieldList,
            ParamStreamDeclaratedField paramStreamDeclaratedField) {
        List<ErrorField> retListSame = new ArrayList<>();
        List<ErrorField> retListOther = new ArrayList<>();

        Stream.of(info.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExecFieldAnnotation.class)).forEach(aField -> {
                    retListSame.addAll(this.validaIfGetValue(info, mandatoryFieldList,
                            ObjectValidation.builder().nameList(paramStreamDeclaratedField.nameList)
                                    .props(paramStreamDeclaratedField.props).aField(aField)
                                    .listDependenciaSame(paramStreamDeclaratedField.listDependenciaSame)
                                    .isOtherOperation(false).build()));
                    retListOther.addAll(this.validaIfGetValue(info, mandatoryFieldList,
                            ObjectValidation.builder().nameList(paramStreamDeclaratedField.nameList)
                                    .props(paramStreamDeclaratedField.props).aField(aField)
                                    .listDependenciaSame(paramStreamDeclaratedField.listDependenciaOther)
                                    .isOtherOperation(true).build()));

                });
        return ErrorFieldObject.builder().retListSame(retListSame).retListOther(retListOther).build();
    }

    public void validateIsErrorDependentOtherOperation(List<ErrorField> errorField) {
        if (!errorField.isEmpty()) {
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(ERROR_CODE_FIELD_DEPENDENT_OTHER_OPERATION, errorField);
            throw new ValidationException(error);
        }
    }

    public List<ErrorField> validateDependency(
            List<MyClassDependent> listDependencia, PropertyDescriptor currProp, ErrorField errorField) {
        List<ErrorField> retList = new ArrayList<>();
        Optional<MyClassDependent> opDep = listDependencia.stream()
                .filter(pre -> pre.getMyField().equals(currProp.getName())).findFirst();
        if (opDep.isPresent()) {
            if (opDep.get().isMandatory()) {
                retList.add(errorField);
            }
        } else {
            retList.add(errorField);
        }
        return retList;
    }
}