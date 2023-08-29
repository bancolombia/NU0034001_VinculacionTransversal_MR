package co.com.bancolombia.usecase.merge;

import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;

@RequiredArgsConstructor
public class MergeUseCaseImpl implements MergeUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, PRODUCT_VTN, this.getClass().getName());

    /**
     * Retrieves the property descriptors of the fields of the Object OldData...
     * @param oldData
     * @return List of property descriptors.
     */
    public List<PropertyDescriptor> getAnnotatedFieldsProperties(Mergeable oldData) {
        List<PropertyDescriptor> availableFields = new ArrayList<>();
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(oldData.getClass()).getPropertyDescriptors();
            Arrays.stream(oldData.getClass().getDeclaredFields())
                  .filter(field -> field.isAnnotationPresent(ExecFieldAnnotation.class)).forEach(
                currField -> Arrays.stream(props).filter(prop -> prop.getName().equals(currField.getName()))
                                   .findFirst().ifPresent(availableFields::add));
        } catch (IntrospectionException e) {
            adapter.error(Level.SEVERE + e.getMessage());
        }
        return availableFields;
    }

    public List<ErrorField> merge(
            Mergeable oldData, Mergeable newData, MergeAttrib mergeAttrib) {
        List<ExecFieldReply> noUpFields = vinculationUpdateUseCase.checkListStatus(
                oldData.getAcquisition().getId(), mergeAttrib.getStepCode()).getExecFieldList()
                .stream().filter(ExecFieldReply::isNotUpgradeable).collect(Collectors.toList());
        List<ErrorField> errorFields = new ArrayList<>();
        List<PropertyDescriptor> props = this.getAnnotatedFieldsProperties(oldData);
        props.forEach(currProp -> {
            try {
                Object valueInfo = currProp.getReadMethod().invoke(oldData);
                Object valueData = currProp.getReadMethod().invoke(newData);
                if ((valueInfo == null && valueData != null) ||
                        (valueData != null && valueInfo.hashCode() != valueData.hashCode())) {
                    String nameField = currProp.getName();
                    if (noUpFields.stream().anyMatch(nup -> nup.getName().equals(nameField))
                            && !mergeAttrib.isRecordUpgradeable()) {
                        errorFields.add(ErrorField.builder().name(nameField)
                                .complement(mergeAttrib.getNameList()).build());
                    } else {
                        currProp.getWriteMethod().invoke(oldData, valueData);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                adapter.error(Level.SEVERE + e.getMessage());
            }
        });
        return errorFields;
    }
}