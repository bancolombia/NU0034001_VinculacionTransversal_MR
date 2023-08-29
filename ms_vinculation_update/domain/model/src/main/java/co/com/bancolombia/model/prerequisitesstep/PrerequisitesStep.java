package co.com.bancolombia.model.prerequisitesstep;

import co.com.bancolombia.commonsvnt.common.auditing.Auditing;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class PrerequisitesStep extends Auditing {
    private UUID id;
    private TypeAcquisition typeAcquisition;
    private Step currentStep;
    private Step step;
    private List <String> states;
    private boolean active;
}
