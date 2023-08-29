package co.com.bancolombia.markcustomer;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.model.markcustomer.Meta;
import co.com.bancolombia.model.markcustomer.RegisterControlListError;
import co.com.bancolombia.model.markcustomer.RegisterControlListErrorResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListResponse;
import co.com.bancolombia.model.markcustomer.RegisterControlListTotal;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class MarkCustomerValidationsUseCaseTest {

    @InjectMocks
    @Spy
    private MarkCustomerValidationsUseCaseImpl markCustomerUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationException.class)
    public void startProcessSuitableNoReportTest() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().response(RegisterControlListResponse.builder()
                .meta(Meta.builder().build()).build()).errors(RegisterControlListErrorResponse.builder()
                .errors(Collections.singletonList(RegisterControlListError.builder().code("S").detail("").title("")
                        .status("").build())).build()).build();
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        this.markCustomerUseCase.validateResponse(registerControlListTotal.getErrors(), acquisition);
        Mockito.verify(this.markCustomerUseCase, Mockito.times(1))
                .validateResponse(registerControlListTotal.getErrors(), acquisition);
    }

    @Test(expected = ValidationException.class)
    public void startProcessSuitableNoReportWithoutSTest() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().response(RegisterControlListResponse.builder()
                .meta(Meta.builder().build()).build()).errors(RegisterControlListErrorResponse.builder()
                .errors(Collections.singletonList(RegisterControlListError.builder().code("B").detail("").title("")
                        .status("").build())).build()).build();
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        this.markCustomerUseCase.validateResponse(registerControlListTotal.getErrors(), acquisition);
        Mockito.verify(this.markCustomerUseCase, Mockito.times(1))
                .validateResponse(registerControlListTotal.getErrors(), acquisition);
    }

    @Test(expected = ValidationException.class)
    public void startProcessSuitableNoReportWithBP8Test() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().response(RegisterControlListResponse.builder()
                .meta(Meta.builder().build()).build()).errors(RegisterControlListErrorResponse.builder()
                .errors(Collections.singletonList(RegisterControlListError.builder().code("BP0008").detail("").title("")
                        .status("").build())).build()).build();
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        this.markCustomerUseCase.validateResponse(registerControlListTotal.getErrors(), acquisition);
        Mockito.verify(this.markCustomerUseCase, Mockito.times(1))
                .validateResponse(registerControlListTotal.getErrors(), acquisition);
    }

    @Test(expected = ValidationException.class)
    public void startProcessSuitableNoReportWithBP16Test() {
        DocumentType documentType = DocumentType.builder().code("").codeOrderControlList("").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("5").build();
        Acquisition acquisition = Acquisition.builder().documentType(documentType).documentNumber("")
                .stateAcquisition(stateAcquisition).id(UUID.randomUUID()).build();
        RegisterControlListTotal registerControlListTotal = RegisterControlListTotal.builder().response(RegisterControlListResponse.builder()
                .meta(Meta.builder().build()).build()).errors(RegisterControlListErrorResponse.builder()
                .errors(Collections.singletonList(RegisterControlListError.builder().code("BP0016").detail("").title("")
                        .status("").build())).build()).build();
        doReturn(EmptyReply.builder().build()).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        this.markCustomerUseCase.validateResponse(registerControlListTotal.getErrors(), acquisition);
        Mockito.verify(this.markCustomerUseCase, Mockito.times(1))
                .validateResponse(registerControlListTotal.getErrors(), acquisition);
    }
}
