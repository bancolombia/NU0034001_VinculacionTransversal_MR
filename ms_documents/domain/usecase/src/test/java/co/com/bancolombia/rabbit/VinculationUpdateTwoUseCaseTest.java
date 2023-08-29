package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
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
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class VinculationUpdateTwoUseCaseTest {

    @InjectMocks
    @Spy
    private VinculationUpdateTwoUseCaseImpl vinculationUpdateUseCase;

    @Mock
    private DirectAsyncGateway directAsyncGateway;

    @Mock
    private TriggerExceptionUseCase triggerExceptionUseCase;

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getClauseSuccessTest() {
        ClauseReply r = ClauseReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        ClauseReply reply = vinculationUpdateUseCase.getClause("CLAUSE002");
        assertTrue(reply.isValid());
    }

    @Test
    public void getClauseNoValidTest() {
        ClauseReply r = ClauseReply.builder().valid(false).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        ClauseReply reply = vinculationUpdateUseCase.getClause("CLAUSE002");
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        vinculationUpdateUseCase.updateAcquisition(uuid.toString());
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test(expected = ValidationException.class)
    public void updateAcquisitionNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.updateAcquisition(uuid.toString());
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void updateAcquisitionNotValidVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.updateAcquisition(uuid.toString());
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    private Map<String, List<ErrorField>> getErrorList() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);
        return error;
    }
}
