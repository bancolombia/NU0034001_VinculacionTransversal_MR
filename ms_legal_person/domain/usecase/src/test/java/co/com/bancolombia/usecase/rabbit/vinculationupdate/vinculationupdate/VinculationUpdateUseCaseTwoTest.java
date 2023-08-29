package co.com.bancolombia.usecase.rabbit.vinculationupdate.vinculationupdate;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateCountReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseTwo;
import co.com.bancolombia.usecase.util.rabbit.DependentFieldSearchInput;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class VinculationUpdateUseCaseTwoTest {

    @InjectMocks
    @Spy
    VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo;

    @Mock
    private DirectAsyncGateway directAsyncGateway;
    @Mock
    private TriggerExceptionUseCase triggerExceptionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkListStatusSuccessTest(){
        ChecklistReply r = ChecklistReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        vinculationUpdateUseCaseTwo.checkListStatus(UUID.randomUUID(), "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test(expected = ValidationException.class)
    public void checkListStatusNotValidTest(){
        ChecklistReply r = ChecklistReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCaseTwo.checkListStatus(UUID.randomUUID(), "1");
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void checkListStatusTriggerTest(){
        ChecklistReply r = ChecklistReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(anyMap());
        vinculationUpdateUseCaseTwo.checkListStatus(UUID.randomUUID(), "1");
        verify(vinculationUpdateUseCaseTwo, times(1)).
                checkListStatus(any(UUID.class), anyString());
    }

    @Test
    public void validateCatalogSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        Map<String, List<GeographicQuery>> catalogGeo = new HashMap<>();
        vinculationUpdateUseCaseTwo.validateCatalog(catalog, catalogGeo);
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test
    public void validateCatalogTriggerTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(anyMap());
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        Map<String, List<GeographicQuery>> catalogGeo = new HashMap<>();
        vinculationUpdateUseCaseTwo.validateCatalog(catalog, catalogGeo);
        verify(vinculationUpdateUseCaseTwo, times(1)).validateCatalog(anyMap(), anyMap());
    }

    @Test(expected = ValidationException.class)
    public void validateCatalogNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        Map<String, List<GeographicQuery>> catalogGeo = new HashMap<>();
        vinculationUpdateUseCaseTwo.validateCatalog(catalog, catalogGeo);
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void dependentFieldNormalTest() {
        DependentFieldReply r = DependentFieldReply.builder().build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        vinculationUpdateUseCaseTwo.dependentFieldNormal("001", "1", new ArrayList<>(), true);
        verify(this.vinculationUpdateUseCaseTwo, times(1)).
                dependentFieldNormal("001", "1", new ArrayList<>(), true);
    }

    @Test
    public void dependentFieldSearchTest() {
        DependentFieldReply r = DependentFieldReply.builder().build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        UUID idAcq = UUID.randomUUID();
        DependentFieldSearchInput input = DependentFieldSearchInput.builder()
                .idAcq(idAcq).table("table").searchField("1")
                .searchValue("asd").active(true).build();
        vinculationUpdateUseCaseTwo.dependentFieldSearch(input);
        verify(this.vinculationUpdateUseCaseTwo, times(1)).
                dependentFieldSearch(any(DependentFieldSearchInput.class));
    }

    private Map<String, List<ErrorField>> getErrorList() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);
        return error;
    }

    @Test
    public void countAcquisitionByStateTest(){
        List<UUID> acquisitionIdList = new ArrayList<>();
        acquisitionIdList.add(UUID.fromString("919a8157-6beb-4627-bc60-c031d384da3c"));
        AcquisitionStateCountReply reply = AcquisitionStateCountReply.builder().valid(true).acquisitionNumber(1).build();
        doReturn(Mono.just(reply)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        assertNotNull(vinculationUpdateUseCaseTwo.countAcquisitionByState("1",acquisitionIdList));
    }

    @Test(expected = ValidationException.class)
    public void countAcquisitionByStateExceptionTest(){
        List<UUID> acquisitionIdList = new ArrayList<>();
        acquisitionIdList.add(UUID.fromString("919a8157-6beb-4627-bc60-c031d384da3c"));

        HashMap<String, List<ErrorField>> errors = new HashMap<>();
        AcquisitionStateCountReply reply = AcquisitionStateCountReply.builder().valid(false).errorList(errors).build();

        doReturn(Mono.just(reply)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCaseTwo.countAcquisitionByState("1",acquisitionIdList);
        verify(this.vinculationUpdateUseCaseTwo, times(1)).countAcquisitionByState(anyString(),anyList());
    }

    @Test
    public void countAcquisitionByStateTriggerTest(){
        List<UUID> acquisitionIdList = Collections.singletonList(UUID.fromString("919a8157-6beb-4627-bc60-c031d384da3c"));
        AcquisitionStateCountReply r = AcquisitionStateCountReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(any(AsyncQuery.class), anyString(), any(Class.class));
        doNothing().when(triggerExceptionUseCase).trigger(anyMap());
        vinculationUpdateUseCaseTwo.countAcquisitionByState("1",acquisitionIdList);
        verify(this.vinculationUpdateUseCaseTwo, times(1)).countAcquisitionByState(anyString(),anyList());
    }

}