package co.com.bancolombia.model.step.gateways;

import co.com.bancolombia.commonsvnt.model.step.Step;

import java.util.Optional;

public interface StepRepository {

    public Optional<Step> findByCode(String code);

    public Step save(Step step);
}
