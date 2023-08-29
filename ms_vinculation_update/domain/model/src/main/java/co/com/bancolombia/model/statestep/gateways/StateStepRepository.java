package co.com.bancolombia.model.statestep.gateways;

import co.com.bancolombia.commonsvnt.model.statestep.StateStep;

import java.util.Optional;

public interface StateStepRepository {

    public Optional<StateStep> findByCode(String code);
}
