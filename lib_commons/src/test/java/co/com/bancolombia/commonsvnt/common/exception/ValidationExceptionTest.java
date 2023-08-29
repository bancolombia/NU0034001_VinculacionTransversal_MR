package co.com.bancolombia.commonsvnt.common.exception;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ValidationExceptionTest {

    @Test
    public void validateValidationExceptionTest() {
        Map<String, List<ErrorField>> errorFieldsCodes = new HashMap<>();
        ValidationException validationException1 = new ValidationException(errorFieldsCodes);
        ValidationException validationException2 = new ValidationException(errorFieldsCodes);

        assertNotNull(validationException1);
        assertNotNull(validationException1.toString());
        assertNotNull(validationException1.hashCode());
        Assert.assertEquals(validationException1,validationException2);
    }
}