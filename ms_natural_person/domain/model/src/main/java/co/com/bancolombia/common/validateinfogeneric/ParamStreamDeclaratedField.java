package co.com.bancolombia.common.validateinfogeneric;

import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.dependentfield.MyClassDependent;
import lombok.Builder;
import lombok.Data;

import java.beans.PropertyDescriptor;
import java.util.List;

@Data
@Builder
public class ParamStreamDeclaratedField {
    public String nameList;
    public DependentFieldParamValidator depFieldParamVal;
    public PropertyDescriptor[] props;
    public List<MyClassDependent> listDependenciaSame;
    public List<MyClassDependent> listDependenciaOther;
}
