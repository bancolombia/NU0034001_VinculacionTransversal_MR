package co.com.bancolombia.usecase.rabbit.vinculationupdate.vinculationupdate;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TriggerUseCaseTest {

    @InjectMocks
    @Spy
    private TriggerExceptionUseCaseImpl triggerExceptionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationException.class)
    public void trigger() {
        Map<String, List<ErrorField>> map = new HashMap<>();
        triggerExceptionUseCase.trigger(map);
    }
}