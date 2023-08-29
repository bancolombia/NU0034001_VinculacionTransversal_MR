package co.com.bancolombia.stateacquisition;

import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;

import java.util.Optional;

public interface StateAcquisitionUseCase {

    /**
     * This function searches the state acquisition by its code
     *
     * @param code
     * @return Optional<StateAcquisition>
     */
    Optional<StateAcquisition> findByCode(String code);

    /**
     * Saves a state acquisition.
     *
     * @param stateAcquisition
     */
    void saveStateAcquisition(StateAcquisition stateAcquisition);
}
