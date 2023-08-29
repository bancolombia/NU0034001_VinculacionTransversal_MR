package co.com.bancolombia.model.stateacquisition.gateways;

import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;

import java.util.Optional;

public interface StateAcquisitionRepository {
    Optional<StateAcquisition> findByCode(String code);

    StateAcquisition save(StateAcquisition stateAcquisition);
}
