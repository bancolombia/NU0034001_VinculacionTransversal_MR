package co.com.bancolombia.commonsvnt.usecase.util;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
public class ValidationJsonFieldsTest {

    @InjectMocks
    ValidationJsonFields validationJsonFields;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validationEmailTest() {
        assertTrue(ValidationJsonFields.validationEmail(null));
    }

    @Test
    public void validationEmailNotRegexTest() {
        assertTrue(ValidationJsonFields.validationEmail("juan@.com"));
    }

    @Test
    public void validationEmailRegexTest() {
        assertTrue(ValidationJsonFields.validationEmail("juan@gmail.com"));
    }

    @Test
    public void validationEmailFirstPartitionTest() {
        assertFalse(ValidationJsonFields.validationEmail("sininformacion@gmail.com"));
    }

    @Test
    public void validationEmailSecondPartitionTest() {
        assertFalse(ValidationJsonFields.validationEmail("aaaaaa@sininformacion.com"));
    }

    @Test
    public void validationEmailFirstSecondPartitionTest() {
        assertFalse(ValidationJsonFields.validationEmail("sininformacion@sininformacion.com"));
    }

    @Test
    public void validationInjectionHTMLNullTest(){
        assertTrue(ValidationJsonFields.validationInjectionHTML(null));
    }

    @Test
    public void validationInjectionHTMLEmptyTest(){
        assertTrue(ValidationJsonFields.validationInjectionHTML(""));
    }

    @Test
    public void validationInjectionHTMLRegexTest(){
        assertFalse(ValidationJsonFields.validationInjectionHTML("<>aaaaa<>"));
    }
}
