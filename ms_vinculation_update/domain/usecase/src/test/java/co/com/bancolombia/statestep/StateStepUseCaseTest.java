package co.com.bancolombia.statestep;

import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.model.statestep.gateways.StateStepRepository;
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
public class StateStepUseCaseTest {

    @InjectMocks
    @Spy
    private StateStepUseCaseImpl stateStepUseCase;

    @Mock
    private StateStepRepository stateStepRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByCodeTest() {
        StateStep stateStep = StateStep.builder().code("1").name("Pendiente").build();
        Mockito.doReturn(Optional.of(stateStep)).when(stateStepRepository).findByCode(any(String.class));
        Optional<StateStep> tcc = stateStepUseCase.findByCode("1");
        assertNotNull(tcc);
    }
}
