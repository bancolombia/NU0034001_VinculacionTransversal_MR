package co.com.bancolombia.dependentfield;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DependentFieldParamValidator {
    private String operation;
    private Object dependentObject;
    private Acquisition acquisition;
}