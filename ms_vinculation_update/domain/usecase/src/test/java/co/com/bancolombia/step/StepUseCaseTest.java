package co.com.bancolombia.step;

import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.model.step.gateways.StepRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RequiredArgsConstructor
public class StepUseCaseTest {

    @InjectMocks
    @Spy
    private StepUseCaseImpl stepUseCase;

    @Mock
    private StepRepository stepRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest(){
        Step step = Step.builder().code("001").active(true).build();
        Mockito.doReturn(Optional.of(step)).when(stepRepository).findByCode(any(String.class));
        Optional<Step> tcc = stepUseCase.findByCode("");
        assertNotNull(tcc);
    }

    @Test
    public void saveStepTest() {
        Step step = Step.builder().build();
        Mockito.doReturn(step).when(stepRepository).save(any(Step.class));
        stepUseCase.saveStep(step);
        Mockito.verify(stepRepository, Mockito.times(1)).save(any(Step.class));
    }
}
