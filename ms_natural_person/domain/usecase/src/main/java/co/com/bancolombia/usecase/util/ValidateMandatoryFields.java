package co.com.bancolombia.usecase.util;

import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import lombok.RequiredArgsConstructor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;

@RequiredArgsConstructor
public class ValidateMandatoryFields {

    LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, PRODUCT_VTN, this.getClass().getName());

    public List<ErrorField> validateMandatoryFields(Object info, List<ExecFieldReply> mandatoryFieldList) {
        List<ErrorField> ret = new ArrayList<>();
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(info.getClass()).getPropertyDescriptors();
            Stream.of(info.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(ExecFieldAnnotation.class))
                    .forEach(aField -> {
                        String pName = aField.getName();
                        Arrays.stream(props)
                                .filter(prop -> prop.getName().equals(aField.getName()))
                                .findFirst()
                                .ifPresent(currProp -> {
                                    try {
                                        Object value = currProp.getReadMethod().invoke(info);
                                        boolean any = mandatoryFieldList.stream()
                                                .anyMatch(item -> item.getName().equals(pName));
                                        if (value == null && any) {
                                            ret.add(ErrorField.builder().name(currProp.getName()).build());
                                        }
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        adapter.error(Level.SEVERE + e.getMessage());
                                    }
                                });
                    });
        } catch (IntrospectionException e) {
            adapter.error(Level.SEVERE + e.getMessage());
        }
        return ret;
    }
}