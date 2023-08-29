package co.com.bancolombia.statestep;

import co.com.bancolombia.commonsvnt.model.statestep.StateStep;

import java.util.Optional;

public interface StateStepUseCase {

    /**
     * This function searches the state step by its code
     *
     * @param code
     * @return Optional<StateStep>
     */
    public Optional<StateStep> findByCode(String code);
}
