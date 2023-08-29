package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ClauseReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
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
public class VinculationUpdateUseCaseTest {

    @InjectMocks
    @Spy
    private VinculationUpdateUseCaseImpl vinculationUpdateUseCase;

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
    public void validateAcquisitionSuccessTest() {
        AcquisitionReply r = AcquisitionReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        AcquisitionReply reply = vinculationUpdateUseCase.validateAcquisition(
                uuid.toString(), "TIPDOC_FS001", "123456", OPER_UPLOAD_DOCUMENT);
        assertTrue(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void validateAcquisitionNotValidTest() {
        AcquisitionReply r = AcquisitionReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        AcquisitionReply reply = vinculationUpdateUseCase.validateAcquisition(
                uuid.toString(), "TIPDOC_FS001", "123456", OPER_UPLOAD_DOCUMENT);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateAcquisitionNotValidVoidTest() {
        AcquisitionReply r = AcquisitionReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        AcquisitionReply reply = vinculationUpdateUseCase.validateAcquisition(
                uuid.toString(), "TIPDOC_FS001", "123456", OPER_UPLOAD_DOCUMENT);
        assertFalse(reply.isValid());
    }

    @Test
    public void updateAcquisitionSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        vinculationUpdateUseCase.updateAcquisition(uuid.toString(), "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test(expected = ValidationException.class)
    public void updateAcquisitionNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.updateAcquisition(uuid.toString(), "1");
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void updateAcquisitionNotValidVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.updateAcquisition(uuid.toString(), "1");
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void markOperationSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        vinculationUpdateUseCase.markOperation(uuid, OPER_UPLOAD_DOCUMENT, "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test(expected = ValidationException.class)
    public void markOperationNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.markOperation(uuid, OPER_UPLOAD_DOCUMENT, "1");
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void markOperationNotValidVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.markOperation(uuid, OPER_UPLOAD_DOCUMENT, "1");
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void checkListStatusSuccessTest() {
        ChecklistReply r = ChecklistReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        ChecklistReply reply = vinculationUpdateUseCase.checkListStatus(uuid, OPER_UPLOAD_DOCUMENT);
        assertTrue(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void checkListStatusNotValidTest() {
        ChecklistReply r = ChecklistReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        ChecklistReply reply = vinculationUpdateUseCase.checkListStatus(uuid, OPER_UPLOAD_DOCUMENT);
        assertFalse(reply.isValid());
    }

    @Test
    public void checkListStatusNotValidVoidTest() {
        ChecklistReply r = ChecklistReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        ChecklistReply reply = vinculationUpdateUseCase.checkListStatus(uuid, OPER_UPLOAD_DOCUMENT);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateCatalogSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        List<CatalogQuery> c = Arrays.asList(CatalogQuery.builder().code("GENERO_M").parents("GENERO").build());
        catalog.put("TVNT000", c);

        vinculationUpdateUseCase.validateCatalog(catalog, null);
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test(expected = ValidationException.class)
    public void validateCatalogNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        List<CatalogQuery> c = Arrays.asList(CatalogQuery.builder().code("GENERO_M").parents("GENERO").build());
        catalog.put("TVNT000", c);

        vinculationUpdateUseCase.validateCatalog(catalog, null);
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void validateCatalogNotValidVoidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        List<CatalogQuery> c = Arrays.asList(CatalogQuery.builder().code("GENERO_M").parents("GENERO").build());
        catalog.put("TVNT000", c);

        vinculationUpdateUseCase.validateCatalog(catalog, null);
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void findCatalogSuccessTest() {
        CatalogReply r = CatalogReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        CatalogReply reply = vinculationUpdateUseCase.findCatalog("GENERO_M", "GENERO");
        assertTrue(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void findCatalogNotValidTest() {
        CatalogReply r = CatalogReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        CatalogReply reply = vinculationUpdateUseCase.findCatalog("GENERO_M", "GENERO");
        assertFalse(reply.isValid());
    }

    @Test
    public void findCatalogNotValidVoidTest() {
        CatalogReply r = CatalogReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        CatalogReply reply = vinculationUpdateUseCase.findCatalog("GENERO_M", "GENERO");
        assertFalse(reply.isValid());
    }

    @Test
    public void findGeographicSuccessTest() {
        GeographicReply r = GeographicReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        GeographicReply reply = vinculationUpdateUseCase.findGeographic("CITY", "DEPARTMENT", "COUNTRY");
        assertTrue(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void findGeographicNotValidTest() {
        GeographicReply r = GeographicReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        GeographicReply reply = vinculationUpdateUseCase.findGeographic("CITY", "DEPARTMENT", "COUNTRY");
        assertFalse(reply.isValid());
    }

    @Test
    public void findGeographicNotValidVoidTest() {
        GeographicReply r = GeographicReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(any(Map.class));

        GeographicReply reply = vinculationUpdateUseCase.findGeographic("CITY", "DEPARTMENT", "COUNTRY");
        assertFalse(reply.isValid());
    }

    private Map<String, List<ErrorField>> getErrorList() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);
        return error;
    }
}
