package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.markcustomer.MarkCustomer;
import co.com.bancolombia.model.markcustomer.MarkCustomerResponseWithLog;
import co.com.bancolombia.model.markcustomer.Meta;
import co.com.bancolombia.model.markcustomer.RegisterControlListError;
import co.com.bancolombia.model.markcustomer.RegisterControlListErrorResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListRequest;
import co.com.bancolombia.model.markcustomer.RegisterControlListResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListTotal;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRestRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class MarkCustomerUseCaseTest {

    @InjectMocks
    @Spy
    private MarkCustomerUseCaseImpl markCustomerUseCase;

    @Mock
    private RegisterControlListRestRepository restRepository;

    @Mock
    private MarkCustomerSaveUseCase markCustomerSaveUseCase;

    @Mock
    private MarkCustomerValidationsUseCase markCustomerValidationsUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessTest() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        ValidateIdentityReply personalInfoData = ValidateIdentityReply.builder().firstName("").secondName("").firstSurname("").secondSurname("").build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().response(RegisterControlListResponse.builder()
                .meta(Meta.builder().build()).build()).build();
        doReturn(personalInfoData).when(naturalPersonUseCase).validateIdentity(anyString());
        doReturn(MarkCustomerResponseWithLog.builder().controlListResponse(registerControlListTotal)
                .infoReuseCommon(InfoReuseCommon.builder().build()).build())
                .when(restRepository).getRegisterControl(any(RegisterControlListRequest.class), anyString(), any(Date.class));
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doNothing().when(markCustomerSaveUseCase).saveInfo(any(RegisterControlListResponse.class),
                any(Acquisition.class), any(BasicAcquisitionRequest.class));
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        MarkCustomer markCustomer = this.markCustomerUseCase.startProcessMarkOperation(BasicAcquisitionRequest.builder()
                .messageId("").build(), acquisition);
        assertNotNull(markCustomer);
    }

    @Test
    public void startProcessSuitableTest() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        ValidateIdentityReply personalInfoData = ValidateIdentityReply.builder().firstName("").secondName("").firstSurname("").secondSurname("").build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().response(RegisterControlListResponse.builder()
                .meta(Meta.builder().build()).build()).build();
        doReturn(personalInfoData).when(naturalPersonUseCase).validateIdentity(anyString());
        doReturn(MarkCustomerResponseWithLog.builder().controlListResponse(registerControlListTotal)
                .infoReuseCommon(InfoReuseCommon.builder().build()).build())
                .when(restRepository).getRegisterControl(any(RegisterControlListRequest.class), anyString(), any(Date.class));
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doNothing().when(markCustomerSaveUseCase).saveInfo(any(RegisterControlListResponse.class),
                any(Acquisition.class), any(BasicAcquisitionRequest.class));
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        MarkCustomer markCustomer = this.markCustomerUseCase.startProcessMarkOperation(BasicAcquisitionRequest.builder()
                .messageId("").build(), acquisition);
        assertNotNull(markCustomer);
    }

    @Test
    public void startProcessSuitableErrorTest() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        ValidateIdentityReply personalInfoData = ValidateIdentityReply.builder().firstName("").secondName("").firstSurname("").secondSurname("").build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().errors(RegisterControlListErrorResponse.builder()
                .errors(Collections.singletonList(RegisterControlListError.builder().code("BP0016").detail("").title("")
                        .status("").build())).build()).build();
        doReturn(personalInfoData).when(naturalPersonUseCase).validateIdentity(anyString());
        doReturn(MarkCustomerResponseWithLog.builder().controlListResponse(registerControlListTotal)
                .infoReuseCommon(InfoReuseCommon.builder().build()).build())
                .when(restRepository).getRegisterControl(any(RegisterControlListRequest.class), anyString(), any(Date.class));
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doNothing().when(markCustomerValidationsUseCase).validateResponse(any(RegisterControlListErrorResponse.class), any(Acquisition.class));
        MarkCustomer markCustomer = this.markCustomerUseCase.startProcessMarkOperation(BasicAcquisitionRequest.builder()
                .messageId("").build(), acquisition);
        assertNotNull(markCustomer);
    }
}
