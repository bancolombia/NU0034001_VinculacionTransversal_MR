package co.com.bancolombia.statestep;

import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.model.statestep.gateways.StateStepRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class StateStepUseCaseImpl implements StateStepUseCase {

    private final StateStepRepository stateStepRepository;

    @Override
    public Optional<StateStep> findByCode(String code) {
        return this.stateStepRepository.findByCode(code);
    }
}
