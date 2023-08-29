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
    public void identityRetriesConstantTest() {
        String retries = "IDENTITY_RETRIES";
        assertEquals(constants.IDENTITY_RETRIES, retries);
    }

    @Test
    public void rutRetriesConstantTest() {
        String retries = "RUT_RETRIES";
        assertEquals(constants.RUT_RETRIES, retries);
    }
}
