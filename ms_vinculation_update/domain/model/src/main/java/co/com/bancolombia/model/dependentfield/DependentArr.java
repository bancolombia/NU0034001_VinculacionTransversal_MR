package co.com.bancolombia.model.dependentfield;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class DependentArr {
    List<MyClassDependent> dependentSameOperatation;
    List<MyClassDependent> dependentOtherOperatation;
}