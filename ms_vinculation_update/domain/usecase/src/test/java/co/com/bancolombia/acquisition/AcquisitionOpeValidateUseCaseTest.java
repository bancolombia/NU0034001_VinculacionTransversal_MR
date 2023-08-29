package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.customercontrol.CustomerControlUseCase;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
import co.com.bancolombia.model.customercontrol.CustomerControl;
import co.com.bancolombia.model.validatesession.ValidateSession;
import co.com.bancolombia.model.validatesession.ValidateSessionResponse;
import co.com.bancolombia.validatesession.ValidateSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_UPDATE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class AcquisitionOpeValidateUseCaseTest {

    @InjectMocks
    @Spy
    AcquisitionOpeValidateUseCaseImpl opeValidateUseCase;

    @Mock
    protected CustomerControlUseCase customerControlUseCase;

    @Mock
    protected ValidateSessionUseCase validateSessionUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationException.class)
    public void validateCustomerControlTest(){
        CustomerControl customerControl = CustomerControl.builder().build();
        doReturn(customerControl).when(customerControlUseCase)
                .findByDocumentTypeAndDocumentNumber(anyString(), anyString());
        doReturn("asd").when(customerControlUseCase)
                .unblockCustomer(any(CustomerControl.class));
        opeValidateUseCase.validateCustomerControl("FS001", "12345");
    }

    @Test
    public void validateCustomerControlNullUnblockMessageTest() {
        CustomerControl customerControl = CustomerControl.builder().build();
        doReturn(customerControl).when(customerControlUseCase)
                .findByDocumentTypeAndDocumentNumber(anyString(), anyString());
        doReturn(null).when(customerControlUseCase)
                .unblockCustomer(any(CustomerControl.class));
        opeValidateUseCase.validateCustomerControl("FS001", "12345");
        verify(customerControlUseCase, times(1)).unblockCustomer(any(CustomerControl.class));
    }

    @Test
    public void validateCustomerControlNullCustomerControlTest() {
        doReturn(null).when(customerControlUseCase)
                .findByDocumentTypeAndDocumentNumber(anyString(), anyString());
        opeValidateUseCase.validateCustomerControl("FS001", "12345");
        verify(customerControlUseCase, times(1))
                .findByDocumentTypeAndDocumentNumber(anyString(), anyString());
    }

    @Test
    public void saveValidateSessionTest(){
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        doReturn(infoReuseCommon).when(validateSessionUseCase)
                .getInfoReuseFromValidateSession(any(ValidateSessionResponse.class));
        doReturn(ValidateSession.builder().build()).when(validateSessionUseCase)
                .saveValidateSession(any(ValidateSessionResponse.class), any(Acquisition.class));
        ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder().build();
        Acquisition acquisition = Acquisition.builder()
                .typePerson(TypePerson.builder().code("asd").build())
                .typeAcquisition(TypeAcquisition.builder().code("asd").build())
                .stateAcquisition(StateAcquisition.builder().name("asd").build()).build();
        opeValidateUseCase.saveValidateSession(validateSessionResponse, acquisition, CODE_START_UPDATE);
        verify(validateSessionUseCase, times(1))
                .saveValidateSession(any(ValidateSessionResponse.class), any(Acquisition.class));
    }

    @Test
    public void saveValidateSessionNullValidateSessionResponseTest(){
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().build();
        doReturn(infoReuseCommon).when(validateSessionUseCase)
                .getInfoReuseFromValidateSession(null);
        Acquisition acquisition = Acquisition.builder()
                .typePerson(TypePerson.builder().code("asd").build())
                .typeAcquisition(TypeAcquisition.builder().code("asd").build())
                .stateAcquisition(StateAcquisition.builder().name("asd").build()).build();
        opeValidateUseCase.saveValidateSession(null, acquisition, CODE_START_UPDATE);
        verify(validateSessionUseCase, never())
                .saveValidateSession(any(ValidateSessionResponse.class), any(Acquisition.class));
    }

    @Test
    public void saveValidateSessionNotStartUpdateOperationTest(){
        Acquisition acquisition = Acquisition.builder()
                .typePerson(TypePerson.builder().code("asd").build())
                .typeAcquisition(TypeAcquisition.builder().code("asd").build())
                .stateAcquisition(StateAcquisition.builder().name("asd").build()).build();
        opeValidateUseCase.saveValidateSession(null, acquisition, CODE_START_ACQUISITION);
        verify(validateSessionUseCase, never())
                .getInfoReuseFromValidateSession(any(ValidateSessionResponse.class));
    }

    @Test
    public void getValidateSessionResponseTest() {
        ValidateSessionResponse validateSessionResponse = ValidateSessionResponse.builder().build();
        doReturn(validateSessionResponse).when(validateSessionUseCase).validateValidSession(
                anyString(), anyString(), anyString(), anyString());
        AcquisitionRequestModel acquisition = AcquisitionRequestModel.builder()
                .documentNumber("123").documentType("FS001").token("123").build();
        ValidateSessionResponse response = opeValidateUseCase.getValidateSessionResponse(
                acquisition, CODE_START_UPDATE);
        assertNotNull(response);
    }

    @Test
    public void getValidateSessionResponseStartAcquisitionTest() {
        AcquisitionRequestModel acquisition = AcquisitionRequestModel.builder().build();
        ValidateSessionResponse response = opeValidateUseCase.getValidateSessionResponse(
                acquisition, CODE_START_ACQUISITION);
        assertNull(response);
    }
}
