package co.com.bancolombia.model.dependentmatrixfields;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StepFieldList {
    private String field;
    private boolean mandatory;
    private boolean upgradable;
}
