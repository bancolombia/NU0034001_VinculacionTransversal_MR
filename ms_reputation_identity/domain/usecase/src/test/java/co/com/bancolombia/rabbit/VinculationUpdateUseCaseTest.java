package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_VIN_BLOCK_CUSTOMER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_UPLOAD_DOCUMENT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
                "", "TIPDOC_FS001", "123456", OPER_UPLOAD_DOCUMENT);
        assertFalse(reply.isValid());
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
    public void markOperationSuccessTest() {
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        vinculationUpdateUseCase.markOperation("", OPER_UPLOAD_DOCUMENT, "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }

    @Test
    public void checkListStatusSuccessTest() {
        ChecklistReply r = ChecklistReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        ChecklistReply reply = vinculationUpdateUseCase.checkListStatus("", OPER_UPLOAD_DOCUMENT);
        assertTrue(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void checkListStatusNotValidTest() {
        ChecklistReply r = ChecklistReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        ChecklistReply reply = vinculationUpdateUseCase.checkListStatus("", OPER_UPLOAD_DOCUMENT);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateRulesSuccessTest() {
        ValidateIdentityRulesReply r = ValidateIdentityRulesReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));

        ValidateIdentityRulesReply reply = vinculationUpdateUseCase.validateRules("");
        assertTrue(reply.isValid());
    }

    @Test(expected = ValidationException.class)
    public void validateRulesNotValidTest() {
        ValidateIdentityRulesReply r = ValidateIdentityRulesReply.builder().valid(false).errorList(getErrorList()).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        doThrow(ValidationException.class).when(triggerExceptionUseCase).trigger(any(Map.class));

        ValidateIdentityRulesReply reply = vinculationUpdateUseCase.validateRules("");
        assertFalse(reply.isValid());
    }

    private Map<String, List<ErrorField>> getErrorList() {
        Map<String, List<ErrorField>> error = new HashMap<>();
        List<ErrorField> e = Arrays.asList(ErrorField.builder().build());
        error.put("TVNT000", e);
        return error;
    }

    @Test
    public void blockCustomerTest(){
        EmptyReply r = EmptyReply.builder().valid(true).build();
        doReturn(Mono.just(r)).when(directAsyncGateway).requestReply(
                any(AsyncQuery.class), anyString(), any(Class.class));
        vinculationUpdateUseCase.blockCustomer("", RES_VIN_BLOCK_CUSTOMER, new Date(), "1");
        verify(triggerExceptionUseCase, never()).trigger(any(Map.class));
    }
}
