package co.com.bancolombia.stateacquisition;

import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.model.stateacquisition.gateways.StateAcquisitionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class StateAcquisitionUseCaseImpl implements StateAcquisitionUseCase {

    private final StateAcquisitionRepository stateAcquisitionRepository;

    @Override
    public Optional<StateAcquisition> findByCode(String code) {
        return stateAcquisitionRepository.findByCode(code);
    }

    @Override
    public void saveStateAcquisition(StateAcquisition stateAcquisition) {
        stateAcquisitionRepository.save(stateAcquisition);
    }
}
