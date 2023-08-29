package co.com.bancolombia.usecase.util.constants;

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
    public void errorEconomicConstantTest() {
        String economic = "EconomicInformation no registrada para la adquisición ";
        assertEquals(Constants.ERROR_MSG_ECONOMIC_NOT_FOUND, economic);
    }

    @Test
    public void errorPersonalConstantTest() {
        String personal = "PersonalInformation no registrada para la adquisición ";
        assertEquals(Constants.ERROR_MSG_PERSONAL_NOT_FOUND, personal);
    }

    @Test
    public void errorContactConstantTest() {
        String contact = "ContactInformation no registrada para la adquisición ";
        assertEquals(Constants.ERROR_MSG_CONTACT_NOT_FOUND, contact);
    }
}
