package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.model.uploaddocument.KofaxInformation;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void getRequiredRutSuccessTest() {
        InfoRutReply r = InfoRutReply.builder().valid(true).requiredRut(true).ciiu("CIIU_00000").build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        InfoRutReply reply = naturalPersonUseCase.getRequiredRut(uuid);
        assertTrue(reply.isValid());
    }

    @Test
    public void updateCiiuSuccessValidTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        EmptyReply reply = naturalPersonUseCase.updateCiiu(uuid, "CIIU_00000", "USRMOD");
        assertTrue(reply.isValid());
    }

    @Test
    public void updateCiiuSuccessNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        EmptyReply reply = naturalPersonUseCase.updateCiiu(uuid, "CIIU_00000", "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void updateCiiuExceptionTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        EmptyReply reply = naturalPersonUseCase.updateCiiu(uuid, "CIIU_00000", "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test
    public void updateCiiuExceptionVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        EmptyReply reply = naturalPersonUseCase.updateCiiu(uuid, "CIIU_00000", "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test
    public void savePersonalInfoSuccessValidTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        EmptyReply reply = naturalPersonUseCase.savePersonalInfo(uuid, kofaxInformation, "USRMOD");
        assertTrue(reply.isValid());
    }

    @Test
    public void savePersonalInfoNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        EmptyReply reply = naturalPersonUseCase.savePersonalInfo(uuid, kofaxInformation, "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void savePersonalInfoExceptionTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        EmptyReply reply = naturalPersonUseCase.savePersonalInfo(uuid, kofaxInformation, "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test
    public void savePersonalInfoExceptionVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        KofaxInformation kofaxInformation = KofaxInformation.builder().build();
        EmptyReply reply = naturalPersonUseCase.savePersonalInfo(uuid, kofaxInformation, "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test
    public void saveBasicInfoSuccessValidTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        EmptyReply reply = naturalPersonUseCase.saveBasicInfo(uuid, "GENERO_M", "USRMOD");
        assertTrue(reply.isValid());
    }

    @Test
    public void saveBasicInfoNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        EmptyReply reply = naturalPersonUseCase.saveBasicInfo(uuid, "GENERO_M", "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void saveBasicInfoExceptionTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        EmptyReply reply = naturalPersonUseCase.saveBasicInfo(uuid, "GENERO_M", "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test
    public void saveBasicInfoExceptionVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        EmptyReply reply = naturalPersonUseCase.saveBasicInfo(uuid, "GENERO_M", "USRMOD");
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentitySuccessTest() {
        ValidateIdentityReply r = ValidateIdentityReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        ValidateIdentityReply reply = naturalPersonUseCase.validateIdentity(uuid);
        assertTrue(reply.isValid());
    }

    @Test
    public void getGenerateExposeInfoTest() {
        GenExposeReply r = GenExposeReply.builder().build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        GenExposeReply reply = naturalPersonUseCase.getGenerateExposeInfo(uuid);
        assertNotNull(reply);
    }

    @Test
    public void getSignDocumentReplyTest(){
        SignDocumentReply r = SignDocumentReply.builder().build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(),
                any(Class.class));
        SignDocumentReply reply = naturalPersonUseCase.getSignDocumentReply(uuid);
        assertNotNull(reply);
    }

    private Map<String, List<ErrorField>> getErrorList() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);
        return error;
    }
}
