package co.com.bancolombia.common.validateinfogeneric;

import co.com.bancolombia.dependentfield.MyClassDependent;
import lombok.Builder;
import lombok.Data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

@Data
@Builder
public class ObjectValidation {
    public String nameList;
    public PropertyDescriptor[] props;
    public Field aField;
    public List<MyClassDependent> listDependenciaSame;
    public boolean isOtherOperation;
}
