package co.com.bancolombia.commonsvnt.usecase.util;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ExceptionsTest {

    @InjectMocks
    @Spy
    Exceptions exceptions;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createValidationExceptionNameNN() {
        doNothing().when(exceptions).clearData();
        doNothing().when(exceptions).createErrorField(anyString(), anyString());
        ValidationException validationException = exceptions.createValidationException(
                null, "asd", "asd", "asd");
        assertNotNull(validationException);
    }

    @Test
    public void createValidationExceptionHashNN() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("asd", "asd");
        doNothing().when(exceptions).clearData();
        doNothing().when(exceptions).createErrorField(anyString(), anyString());
        ValidationException validationException = exceptions.createValidationException(
                hashMap, null, null, "asd");
        assertNotNull(validationException);
    }

    @Test
    public void createErrorField() {
        exceptions.createErrorField("asd", "asd");
    }

    @Test(expected = ValidationException.class)
    public void createException() {
        ValidationException validationException = new ValidationException(new HashMap<>());
        doReturn(validationException).when(exceptions).createValidationException(
                any(HashMap.class), anyString(), anyString(), anyString());
        exceptions.createException(null, "asd", "asd", "asd");
    }

    @Test
    public void clearData() {
        exceptions.clearData();
    }
}