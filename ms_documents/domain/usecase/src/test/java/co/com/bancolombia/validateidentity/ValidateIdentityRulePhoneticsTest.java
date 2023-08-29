package co.com.bancolombia.validateidentity;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateIdentityRulePhoneticsTest {

	@InjectMocks
	@Spy
	private ValidateIdentityRulePhoneticsImpl validateIdentityRulePhonetics;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getDistanceTest() {
		LevenshteinDistance distance = new LevenshteinDistance();
		assertEquals(distance.getClass(), validateIdentityRulePhonetics.getDistance().getClass());
	}
	
	@Test
	public void jaroWinklerDistance1Test() {
		boolean res = validateIdentityRulePhonetics.jaroWinklerDistance("juan", "juanca", 90.0);
		assertTrue(res);
	}
	
	@Test
	public void jaroWinklerDistance2Test() {
		boolean res = validateIdentityRulePhonetics.jaroWinklerDistance("juan camilo", "juan andres", 90.0);
		assertFalse(res);
	}

	@Test
	public void jaroWinklerDistance3Test() {
		boolean res = validateIdentityRulePhonetics.jaroWinklerDistance("juan", "juan andres", 90.0);
		assertFalse(res);
	}
}
