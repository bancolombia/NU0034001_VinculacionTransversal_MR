package co.com.bancolombia.model.dependentmatrixfields;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DependentsList {
    private String dependentField;
    private String dependentValue;
    private String dependentOperation;
    private String currentField;
    private boolean mandatory;
}