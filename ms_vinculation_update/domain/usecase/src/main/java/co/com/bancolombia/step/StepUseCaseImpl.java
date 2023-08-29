package co.com.bancolombia.step;

import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.model.step.gateways.StepRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class StepUseCaseImpl implements StepUseCase {

    private final StepRepository stepRepository;

    @Override
    public Optional<Step> findByCode(String code) {
        return this.stepRepository.findByCode(code);
    }

    @Override
    public Step saveStep(Step step) {
        return this.stepRepository.save(step);
    }
}
