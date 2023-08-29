package co.com.bancolombia.step;

import co.com.bancolombia.commonsvnt.model.step.Step;

import java.util.Optional;

public interface StepUseCase {

    /**
     * This function searches the step by its code
     *
     * @param code
     * @return Optional<Step>
     */
    public Optional<Step> findByCode(String code);

    /**
     * Saves a step.
     *
     * @param step
     * @return Step
     */
    public Step saveStep(Step step);
}
