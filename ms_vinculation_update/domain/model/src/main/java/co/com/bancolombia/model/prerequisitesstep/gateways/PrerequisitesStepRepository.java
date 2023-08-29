package co.com.bancolombia.model.prerequisitesstep.gateways;

import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.prerequisitesstep.PrerequisitesStep;

import java.util.List;

public interface PrerequisitesStepRepository {
    public List<PrerequisitesStep> findByTypeAcquisitionAndCurrentStep(TypeAcquisition typeAcquisition,
                                                                       Step currentStep);
}
