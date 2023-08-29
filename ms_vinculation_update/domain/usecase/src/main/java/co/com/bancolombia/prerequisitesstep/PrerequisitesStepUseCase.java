package co.com.bancolombia.prerequisitesstep;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.model.prerequisitesstep.PrerequisitesStep;

import java.util.List;

public interface PrerequisitesStepUseCase {

    public List<PrerequisitesStep> findByTypeAcquisitionAndCurrentStep(TypeAcquisition typeAcquisition,
                                                                       String currentStepCode);

    public void validatePrerrequisites(Acquisition acquisition, String currentStepCode);
}
