package co.com.bancolombia.stateacquisition;

import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.model.stateacquisition.gateways.StateAcquisitionRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class StateAcquisitionUseCaseTest {

    @InjectMocks
    @Spy
    private StateAcquisitionUseCaseImpl stateAcquisitionUseCase;

    @Mock
    private StateAcquisitionRepository stateAcquisitionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findStateAcquisitionTest() {
        StateAcquisition sa = StateAcquisition.builder().code("001").build();
        Mockito.doReturn(Optional.of(sa)).when(stateAcquisitionRepository).findByCode(any(String.class));
        Optional<StateAcquisition> saa = stateAcquisitionUseCase.findByCode("");
        assertNotNull(saa);
    }

    @Test
    public void saveStateAcquisitionTest() {
        StateAcquisition sa = StateAcquisition.builder().build();
        Mockito.doReturn(sa).when(stateAcquisitionRepository).save(any(StateAcquisition.class));
        stateAcquisitionUseCase.saveStateAcquisition(sa);
        verify(this.stateAcquisitionRepository, times(1)).save(any(StateAcquisition.class));
    }
}
