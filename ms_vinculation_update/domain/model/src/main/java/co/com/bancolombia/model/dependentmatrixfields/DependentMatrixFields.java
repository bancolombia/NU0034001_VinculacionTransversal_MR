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
public class DependentMatrixFields {
    private String acquisitionCode;
    private String acquisitionName;
    private List<StepList> stepList;
}
