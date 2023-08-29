package co.com.bancolombia.dependentfield;

import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class DependentFieldObject {
    public List<DependentField> listMappeadoOtherOperation;
    public List<DependentField> listMappeadoSameOperation;
}
