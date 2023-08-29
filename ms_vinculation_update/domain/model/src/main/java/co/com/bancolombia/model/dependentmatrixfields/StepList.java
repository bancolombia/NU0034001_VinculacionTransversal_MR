package co.com.bancolombia.model.dependentmatrixfields;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StepList {
    private String stepCode;
    private List<StepFieldList> stepFieldList;
    private String listOperation;
    private List<StepFieldList> list;
    private List<DependentsList> dependentsLists;
}
