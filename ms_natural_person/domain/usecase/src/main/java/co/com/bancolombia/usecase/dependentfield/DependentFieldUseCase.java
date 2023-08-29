package co.com.bancolombia.usecase.dependentfield;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import co.com.bancolombia.dependentfield.DependentArr;
import co.com.bancolombia.dependentfield.DependentFieldObject;
import co.com.bancolombia.dependentfield.MyClass;
import co.com.bancolombia.dependentfield.MyClassDependent;
import co.com.bancolombia.dependentfield.ObjectParamValidateInverse;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.taxinformation.gateways.TaxInformationRepository;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.C_NOT_NULL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.C_NULL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_FOREIGN_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;

@RequiredArgsConstructor
@AllArgsConstructor
public class DependentFieldUseCase {
    private LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_FOREIGN_INF);
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final TaxInformationRepository taxInformationRepository;

    public List<MyClass> getMapField(Object obj) {
        List<MyClass> myClassList = new ArrayList<>();
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();
            Stream.of(props).forEach(item -> {
                Optional<PropertyDescriptor> op = Stream.of(props)
                        .filter(predicate -> predicate.getName().equals(item.getName())).findFirst();
                MyClass myClass = MyClass.builder().myKey(item.getName()).myMethod(op.get()).build();
                myClassList.add(myClass);
            });
        } catch (IntrospectionException e) {
            adapter.error(Level.SEVERE+e.getMessage());
        }
        return myClassList;
    }

    public List<DependentField> getAllDependentFields(String codeAcq, List<MyClass> map, String currentOpe) {
        return vinculationUpdateUseCase.dependentFieldNormal(
                codeAcq, currentOpe, map.stream().map(MyClass::getMyKey).collect(Collectors.toList()), true)
                .getDependentFields();
    }

    public boolean validateBigDecimal(
            Class<?> clase, DependentField filterOnlySameCurrentField, MyClass valueOb, Object obj) {
        if (BigDecimal.class.isAssignableFrom(clase)) {
            BigDecimal valueObject = null;
            try {
                valueObject = (BigDecimal) valueOb.myMethod.getReadMethod().invoke(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                adapter.error(Level.SEVERE+e.getMessage());
            }
            BigDecimal valueDependent = new BigDecimal(filterOnlySameCurrentField.getDependentValue());
            if (valueObject != null) {
                return valueObject.equals(valueDependent);
            }
        } else {
            try {
                return filterOnlySameCurrentField.getDependentValue()
                        .equals(valueOb.myMethod.getReadMethod().invoke(obj));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                adapter.error(Level.SEVERE+e.getMessage());
            }
        }
        return false;
    }

    public boolean validateValueInDependent(DependentField filterOnlySameCurrentField, MyClass valueOb, Object obj) {
        boolean result = false;
        try {
            Class<?> clase = valueOb.myMethod.getPropertyType();
            if (filterOnlySameCurrentField.getDependentValue().equals(C_NOT_NULL)) {
                BigDecimal valueObject = (BigDecimal) valueOb.myMethod.getReadMethod().invoke(obj);
                result = valueObject != null;
            } else {
                if (filterOnlySameCurrentField.getDependentValue().equals(C_NULL)) {
                    BigDecimal valueObject = (BigDecimal) valueOb.myMethod.getReadMethod().invoke(obj);
                    result = valueObject == null;
                } else {
                    result = validateBigDecimal(clase, filterOnlySameCurrentField, valueOb, obj);
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            adapter.error(Level.SEVERE+e.getMessage());
        }
        return result;
    }

    public boolean validateDependentFieldWhenSameOperation(
            DependentField filterOnlySameCurrentField, List<MyClass> mapeado, Object obj) {
        Optional<MyClass> valueOb = mapeado.stream().filter(
                p -> p.myKey.equals(filterOnlySameCurrentField.getFieldDependent())).findFirst();
        return valueOb.filter(myClass -> validateValueInDependent(
                filterOnlySameCurrentField, myClass, obj)).isPresent();
    }

    public boolean valDepenFieldWhenSameOperDepenInverse(
            DependentField filterOnlySameCurrentField, List<MyClass> mapeado, Object obj) {
        Optional<MyClass> valueOb = mapeado.stream().filter(
                p -> p.myKey.equals(filterOnlySameCurrentField.getCurrentField())).findFirst();
        boolean b = false;
        if (valueOb.isPresent()) {
            try {
                b = filterOnlySameCurrentField.getCurrentField()
                        .equals(valueOb.get().myMethod.getReadMethod().invoke(obj));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                adapter.error(Level.SEVERE+e.getMessage());
            }
        }
        return b;
    }

    public boolean findValueInObjectDepedent(DependentField filterOnlySameCurrentField, Object objectDependent) {
        List<MyClass> mapeado = getMapField(objectDependent);
        return validateDependentFieldWhenSameOperation(filterOnlySameCurrentField, mapeado, objectDependent);
    }

    public boolean validateDependentFieldWhenDifferentOperation(
            DependentField filterOnlySameCurrentField, Acquisition acq,
            Object objectDependent, boolean isDependentInverse) {
        List<Object> objBD = new ArrayList<>();
        if (objectDependent == null) {
            if (isDependentInverse) {
                objBD.addAll(taxInformationRepository.searchDependentValueFieldAndActive(
                        acq.getId(), filterOnlySameCurrentField.getDependentTable(),
                        filterOnlySameCurrentField.getFieldDependent(), null, true));
            } else {
                List<Object> findInBd = taxInformationRepository.searchDependentValueFieldAndActive(
                        acq.getId(), filterOnlySameCurrentField.getDependentTable(),
                        filterOnlySameCurrentField.getFieldDependent(), null, true);
                if (!findInBd.isEmpty()) {
                    objBD.addAll(taxInformationRepository.searchDependentValueFieldAndActive(acq.getId(),
                            filterOnlySameCurrentField.getDependentTable(),
                            filterOnlySameCurrentField.getFieldDependent(),
                            filterOnlySameCurrentField.getDependentValue(), true));
                } else {
                    return true;
                }
            }
        } else {
            return findValueInObjectDepedent(filterOnlySameCurrentField, objectDependent);
        }
        return returnValDepenFieldWhenDiffOper(isDependentInverse, objBD, filterOnlySameCurrentField);
    }

    public boolean returnValDepenFieldWhenDiffOper(
            boolean isDependentInverse, List<Object> objBD, DependentField filterOnlySameCurrentField) {
        boolean result = false;
        if (isDependentInverse) {
            if (objBD.isEmpty()) {
                result = true;
            } else {
                result = fieldIsNull(objBD.get(0), filterOnlySameCurrentField);
            }
            return result;
        } else {
            return (!objBD.isEmpty());
        }
    }

    public boolean fieldIsNull(Object objBd, DependentField fieldFind) {
        List<MyClass> mapeadoBd = getMapField(objBd);
        List<Object> resultObject = new ArrayList<>();
        mapeadoBd.stream().filter(predicate -> predicate.myKey.equals(fieldFind.getFieldDependent())).findFirst()
                .ifPresent(currProp -> {
                    try {
                        Object value = currProp.getMyMethod().getReadMethod().invoke(objBd);
                        if (value != null) {
                            resultObject.add(value);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        adapter.error(Level.SEVERE+e.getMessage());
                    }
                });
        return resultObject.isEmpty();
    }

    public boolean filterOnlySameCurrentField(
            ObjectParamValidateInverse objectParamValidateInverse, DependentField filterOnlySameCurrentField) {
        if (filterOnlySameCurrentField.getCurrentOperation()
                .equals(filterOnlySameCurrentField.getDependentOperation())) {
            return valDepenFieldWhenSameOperDepenInverse(
                    filterOnlySameCurrentField, objectParamValidateInverse.mapeado, objectParamValidateInverse.obj);
        } else {
            return validateDependentFieldWhenDifferentOperation(filterOnlySameCurrentField,
                    objectParamValidateInverse.acq, objectParamValidateInverse.objectDependent, true);
        }
    }

    public List<MyClassDependent> validateFieldDependentInverse(ObjectParamValidateInverse objectParamValidateInverse) {
        List<DependentField> dependentFieldList = new ArrayList<>();
        objectParamValidateInverse.mapeado.stream()
                .forEach(item -> dependentFieldList.addAll(
                        objectParamValidateInverse.depFields.stream()
                                .filter(p -> !p.getCurrentValue().isEmpty())
                                .filter(filterDepFields -> filterDepFields.getCurrentField().equals(item.getMyKey()))
                                .filter(p -> {
                                    Optional<MyClass> dp = objectParamValidateInverse.mapeado.stream()
                                            .filter(pr -> pr.myKey.equals(p.getCurrentField())).findFirst();
                                    boolean r = false;
                                    if (dp.isPresent()) {
                                        try {
                                            r = p.getCurrentValue().equals(dp.get().myMethod.getReadMethod()
                                                    .invoke(objectParamValidateInverse.obj));
                                        } catch (IllegalAccessException | IllegalArgumentException
                                                | InvocationTargetException e) {
                                            adapter.error(Level.SEVERE+e.getMessage());
                                        }
                                    }
                                    return r;
                                })
                                .filter(filterOnlySameCurrentField -> filterOnlySameCurrentField(
                                        objectParamValidateInverse, filterOnlySameCurrentField))
                                .collect(Collectors.toList())));
        List<MyClassDependent> myClassDependentList = new ArrayList<>();
        dependentFieldList.forEach(f -> myClassDependentList.add(constructMyClassDependent(f)));
        return myClassDependentList;
    }

    public MyClassDependent constructMyClassDependent(DependentField f) {
        return MyClassDependent.builder().myField(f.getCurrentField()).mandatory(f.isMandatory())
                .currentField(f.getCurrentField()).currentOperation(f.getCurrentOperation())
                .dependentField(f.getFieldDependent()).dependentOperation(f.getDependentOperation()).build();
    }

    public DependentArr principal(Object obj, Acquisition acq, String currentOper, Object objectDependent) {
        List<MyClass> mapeado = getMapField(obj);
        List<DependentField> depFields = getAllDependentFields(acq.getTypeAcquisition().getCode(), mapeado,
                currentOper);
        DependentFieldObject dependentFieldObject = filterDependentFieldObject(mapeado, depFields);
        List<MyClassDependent> dependentFieldListSameOperation = new ArrayList<>();
        dependentFieldListSameOperation.addAll(
                dependentFieldListSameOperationMethod(dependentFieldObject.listMappeadoSameOperation, mapeado, obj));
        List<MyClassDependent> dependentFieldListOtherOperation = new ArrayList<>();
        dependentFieldObject.listMappeadoOtherOperation.stream()
                .filter(filterOnlySameCurrentField -> validateDependentFieldWhenDifferentOperation(
                        filterOnlySameCurrentField, acq, objectDependent, false))
                .collect(Collectors.toList()).forEach(f -> {
            if (objectDependent != null) {
                dependentFieldListSameOperation.add(constructMyClassDependent(f));
            } else {
                dependentFieldListOtherOperation.add(constructMyClassDependent(f));
            }
        });
        return DependentArr.builder().dependentSameOperatation(dependentFieldListSameOperation)
                .dependentOtherOperatation(dependentFieldListOtherOperation).build();
    }

    public DependentFieldObject filterDependentFieldObject(List<MyClass> mapeado, List<DependentField> depFields) {
        List<DependentField> listMappeadoOtherOperation = new ArrayList<>();
        List<DependentField> listMappeadoSameOperation = new ArrayList<>();
        List<DependentField> d = new ArrayList<>();
        mapeado.stream().forEach(item -> {
            d.addAll(depFields.stream().filter(p -> p.getCurrentValue().isEmpty())
                    .filter(filterDepFields -> filterDepFields.getCurrentField().equals(item.getMyKey()))
                    .collect(Collectors.toList()));
            listMappeadoSameOperation
                    .addAll(d.stream()
                            .filter(filterOnlySameCurrentField -> filterOnlySameCurrentField.getCurrentOperation()
                                    .equals(filterOnlySameCurrentField.getDependentOperation()))
                            .collect(Collectors.toList()));
            listMappeadoOtherOperation
                    .addAll(d.stream()
                            .filter(filterOnlySameCurrentField -> !filterOnlySameCurrentField.getCurrentOperation()
                                    .equals(filterOnlySameCurrentField.getDependentOperation()))
                            .collect(Collectors.toList()));
        });
        return DependentFieldObject.builder().listMappeadoSameOperation(listMappeadoSameOperation)
                .listMappeadoOtherOperation(listMappeadoOtherOperation).build();
    }

    public List<MyClassDependent> dependentFieldListSameOperationMethod(
            List<DependentField> listMappeadoSameOperation, List<MyClass> mapeado, Object obj) {
        List<MyClassDependent> dependentFieldListSameOperation = new ArrayList<>();
        listMappeadoSameOperation.stream()
                .filter(filterOnlySameCurrentField -> validateDependentFieldWhenSameOperation(
                        filterOnlySameCurrentField, mapeado, obj))
                .collect(Collectors.toList())
                .forEach(f -> dependentFieldListSameOperation.add(constructMyClassDependent(f)));
        return dependentFieldListSameOperation;
    }
}

