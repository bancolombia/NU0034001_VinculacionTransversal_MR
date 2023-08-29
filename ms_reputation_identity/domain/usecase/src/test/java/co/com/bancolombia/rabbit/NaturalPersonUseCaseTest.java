package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class NaturalPersonUseCaseTest {

    @InjectMocks
    @Spy
    private NaturalPersonUseCaseImpl naturalPersonUseCase;

    @Mock
    private DirectAsyncGateway directAsyncGateway;

    @Mock
    private TriggerExceptionUseCase triggerExceptionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateAcquisitionTest() {
        ValidateIdentityReply acquisitionReply = ValidateIdentityReply.builder().valid(true).build();
        doReturn(Mono.just(acquisitionReply)).when(directAsyncGateway).requestReply(any(), anyString(), any());
        ValidateIdentityReply result = naturalPersonUseCase.validateIdentity("");
        assertNotNull(result);
    }

    @Test(expected = ValidationException.class)
    public void validateAcquisitionNotValidTest() {
        ValidateIdentityReply r = ValidateIdentityReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));
        ValidateIdentityReply reply = naturalPersonUseCase.validateIdentity("");
        assertFalse(reply.isValid());
    }

    private Map<String, List<ErrorField>> getErrorList() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);
        return error;
    }

}
