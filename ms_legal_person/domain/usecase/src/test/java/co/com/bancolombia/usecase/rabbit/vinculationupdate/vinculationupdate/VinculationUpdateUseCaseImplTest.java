package co.com.bancolombia.usecase.rabbit.vinculationupdate.vinculationupdate;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.DependentFieldReply;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.TARGET_VINCULATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class VinculationUpdateUseCaseImplTest {

    @InjectMocks
    @Spy
    VinculationUpdateUseCaseImpl vinculationUpdateUseCase;
    @Mock
    private DirectAsyncGateway directAsyncGateway;
    @Mock
    private TriggerExceptionUseCase triggerExceptionUseCase;
    @Mock
    private VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateAcquisitionTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().valid(true).build();
        doReturn(Mono.just(acquisitionReply)).when(directAsyncGateway).requestReply(any(), anyString(), any());
        AcquisitionReply result = vinculationUpdateUseCase.validateAcquisition("", "", "", "");
        assertNotNull(result);
    }

    @Test(expected = ValidationException.class)
    public void validateAcquisitionNotValidTest() {
        AcquisitionReply r = AcquisitionReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        AcquisitionReply reply = vinculationUpdateUseCase.validateAcquisition(
                "", "TIPDOC_FS001", "123456", TARGET_VINCULATION);
        assertFalse(reply.isValid());
    }

    @Test
    public void markOperationSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        vinculationUpdateUseCase.markOperation(UUID.randomUUID(), OPER_UPLOAD_DOCUMENT, "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test
    public void updateAcquisitionSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        vinculationUpdateUseCase.updateAcquisition("", "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test(expected = ValidationException.class)
    public void updateAcquisitionNotValidTest() {
        EmptyReply r = EmptyReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        vinculationUpdateUseCase.updateAcquisition("", "1");
        verify(triggerExceptionUseCase, times(1)).trigger(any(Map.class));
    }

    @Test
    public void checkListStatusSuccessTest() {
        ChecklistReply r = ChecklistReply.builder().valid(true).build();
        doReturn(r).when(vinculationUpdateUseCaseTwo).checkListStatus(
                any(UUID.class), anyString());
        vinculationUpdateUseCase.checkListStatus(UUID.randomUUID(), "1");
        verify(vinculationUpdateUseCase, times(1)).checkListStatus(any(UUID.class), anyString());
    }


    @Test
    public void validateCatalogSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(r).when(vinculationUpdateUseCaseTwo).validateCatalog(
                anyMap(), anyMap());
        Map<String, List<CatalogQuery>> catalog = new HashMap<>();
        Map<String, List<GeographicQuery>> catalogGeo = new HashMap<>();

        vinculationUpdateUseCase.validateCatalog(catalog, catalogGeo);
        verify(vinculationUpdateUseCase, times(1)).validateCatalog(anyMap(), anyMap());
    }

    @Test
    public void dependentFieldNormalTest() {
        DependentFieldReply r = DependentFieldReply.builder().build();
        doReturn(r).when(vinculationUpdateUseCaseTwo).dependentFieldNormal(
                anyString(), anyString(), anyList(), anyBoolean());

        vinculationUpdateUseCase.dependentFieldNormal("001", "1", new ArrayList<>(), true);
        verify(this.vinculationUpdateUseCase, times(1)).
                dependentFieldNormal(anyString(), anyString(), anyList(), anyBoolean());
    }

    @Test
    public void dependentFieldSearchTest() {
        DependentFieldReply r = DependentFieldReply.builder().build();
        doReturn(r).when(vinculationUpdateUseCaseTwo).dependentFieldSearch(
                any(DependentFieldSearchInput.class));
        UUID idAcq = UUID.randomUUID();
        DependentFieldSearchInput input = DependentFieldSearchInput.builder()
                .idAcq(idAcq).table("table").searchField("1")
                .searchValue("asd").active(true).build();
        vinculationUpdateUseCase.dependentFieldSearch(input);
        verify(vinculationUpdateUseCase, times(1))
                .dependentFieldSearch(any(DependentFieldSearchInput.class));
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
        vinculationUpdateUseCase.countAcquisitionByState("1",acquisitionIdList);
        verify(vinculationUpdateUseCase,times(1)).countAcquisitionByState(anyString(),anyList());
    }
}