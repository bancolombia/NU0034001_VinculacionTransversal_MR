package co.com.bancolombia.validateidentity;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ValidateIdentityRuleUtilUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateIdentityRuleUtilUseCaseImpl validateIdentityRuleUtilUseCase;

    @Mock
    private ValidateIdentityRulePhonetics validateIdentityRulePhonetics;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void jaroWinklerDistanceTest() {
        doReturn(true).when(validateIdentityRulePhonetics).jaroWinklerDistance(
                anyString(), anyString(), anyDouble());

        boolean b = validateIdentityRuleUtilUseCase.jaroWinklerDistance("juan", "jose", 90.0);
        assertTrue(b);
    }

    @Test
    public void compareStringEqualsTest() {
        boolean b = validateIdentityRuleUtilUseCase.compareString("juan", "juan", 90.0);
        assertTrue(b);
    }

    @Test
    public void compareStringDifferentTest() {
        doReturn(true).when(validateIdentityRulePhonetics).jaroWinklerDistance(
                anyString(), anyString(), anyDouble());

        boolean b = validateIdentityRuleUtilUseCase.compareString("juan", "jose", 90.0);
        assertTrue(b);
    }
}
