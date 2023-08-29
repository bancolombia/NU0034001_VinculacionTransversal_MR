package co.com.bancolombia.util.constants;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class ConstantsTest {

    @InjectMocks
    @Spy
    private Constants constants;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test
    public void acquisitionConstantTest() {
        String acquisition = "ADQUISICION";
        assertEquals(constants.ACQUISITION, acquisition);
    }

    @Test
    public void stepConstantTest() {
        String step = "OPERACION";
        assertEquals(constants.STEP, step);
    }

    @Test
    public void stateStepConstantTest() {
        String stateStep = "ESTADO OPERACION";
        assertEquals(constants.STATE_STEP, stateStep);
    }
}
