package co.com.bancolombia.dependentfield;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ObjectParamValidateInverse {
    public Object obj;
    public Acquisition acq;
    public List<MyClass> mapeado;
    public List<DependentField> depFields;
    public Object objectDependent;
}
