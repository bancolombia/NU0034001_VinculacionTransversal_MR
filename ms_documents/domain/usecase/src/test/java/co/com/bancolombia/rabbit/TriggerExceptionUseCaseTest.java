package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TriggerExceptionUseCaseTest {

    @InjectMocks
    @Spy
    private TriggerExceptionUseCaseImpl triggerExceptionUseCase;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this); }

    @Test(expected = ValidationException.class)
    public void triggerTest() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);

        triggerExceptionUseCase.trigger(error);
    }
}
