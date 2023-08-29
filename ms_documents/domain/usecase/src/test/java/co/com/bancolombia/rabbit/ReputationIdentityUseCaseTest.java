package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.ParameterReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
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

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ReputationIdentityUseCaseTest {

    @InjectMocks
    @Spy
    private ReputationIdentityUseCaseImpl reputationIdentityUseCase;

    @Mock
    private DirectAsyncGateway directAsyncGateway;

    @Mock
    private TriggerExceptionUseCase triggerExceptionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getIdentityValResultSuccessTest() {
        IdentityValResultReply r = IdentityValResultReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        IdentityValResultReply reply = reputationIdentityUseCase.getIdentityValResultReply(UUID.randomUUID());
        assertTrue(reply.isValid());
    }

    @Test
    public void getIdentityValResultFalseTest() {
        IdentityValResultReply r = IdentityValResultReply.builder().valid(false).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        IdentityValResultReply reply = reputationIdentityUseCase.getIdentityValResultReply(UUID.randomUUID());
        assertFalse(reply.isValid());
    }

    @Test
    public void getParameterSuccessTest() {
        ParameterReply r = ParameterReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        ParameterReply reply = reputationIdentityUseCase.getParameterByNameAndParent("asd", "asd");
        assertTrue(reply.isValid());
    }

    @Test
    public void getParameterFalseTest() {
        ParameterReply r = ParameterReply.builder().valid(false).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        ParameterReply reply = reputationIdentityUseCase.getParameterByNameAndParent("asd", "asd");
        assertFalse(reply.isValid());
    }


}
