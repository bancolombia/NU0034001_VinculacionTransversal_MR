package co.com.bancolombia.model.dependentfield;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DependentFieldParamValidator {
	private Acquisition acquisition;
    private String operation;
    private Object dependentObject;
}