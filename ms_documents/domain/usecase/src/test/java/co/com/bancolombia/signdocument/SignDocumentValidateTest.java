package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.signdocument.SDResponseError;
import co.com.bancolombia.model.signdocument.SDResponseErrorItem;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateTwoUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_SIGN_DOC_RETRIES;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class SignDocumentValidateTest {

    @InjectMocks
    @Spy
    SignDocumentValidateUseCaseImpl signValidateUseCase;

    @Mock
    ParametersUseCase parametersUseCase;

    @Mock
    Exceptions exceptions;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    VinculationUpdateTwoUseCase vinculationUpdateTwoUseCase;

    private Acquisition acquisition;
    private Date date;

    @Before
    public void setUp() {
        date = new Date();
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID()).documentType(DocumentType.builder()
                        .codeGenericType("asd").build()).documentNumber("asd").build();
    }

    @Test
    public void actionsErrors20Retry() {
        List<SDResponseErrorItem> errorItems = new ArrayList<>();
        errorItems.add(SDResponseErrorItem.builder().code("BP1412").build());
        SDResponseError responseError = SDResponseError.builder().errors(errorItems).build();
        doNothing().when(signValidateUseCase).actionErrorsRetry(any(Acquisition.class), any(SDResponseErrorItem.class));
        doNothing().when(signValidateUseCase).updateOperationsStep(any(Acquisition.class));
        doReturn("asd").when(signValidateUseCase).createErrorComplement(any(SDResponseErrorItem.class));
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        signValidateUseCase.actionsErrors(acquisition, responseError);
        verify(signValidateUseCase, times(1)).actionsErrors(acquisition, responseError);
    }

    @Test
    public void actionsErrors20NoRetry() {
        List<SDResponseErrorItem> errorItems = new ArrayList<>();
        errorItems.add(SDResponseErrorItem.builder().code("BP1401").build());
        SDResponseError responseError = SDResponseError.builder().errors(errorItems).build();
        doNothing().when(signValidateUseCase).updateOperationsStep(any(Acquisition.class));
        doReturn("asd").when(signValidateUseCase).createErrorComplement(any(SDResponseErrorItem.class));
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        signValidateUseCase.actionsErrors(acquisition, responseError);
        verify(this.signValidateUseCase, times(1)).actionsErrors(acquisition, responseError);
    }

    @Test
    public void actionsErrors12() {
        List<SDResponseErrorItem> errorItems = new ArrayList<>();
        errorItems.add(SDResponseErrorItem.builder().code("400").build());
        SDResponseError responseError = SDResponseError.builder().errors(errorItems).build();
        doNothing().when(signValidateUseCase).updateOperationsStep(any(Acquisition.class));
        doReturn("asd").when(signValidateUseCase).createErrorComplement(any(SDResponseErrorItem.class));
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        signValidateUseCase.actionsErrors(acquisition, responseError);
        verify(this.signValidateUseCase, times(1)).actionsErrors(acquisition, responseError);
    }

    @Test
    public void actionErrorsRetry() {
        SDResponseErrorItem errorItem = SDResponseErrorItem.builder().build();
        acquisition.setSignDocRetries(0);
        List<Parameters> parameters = new ArrayList<>();
        parameters.add(Parameters.builder().code("3").parent(PARAMETER_SIGN_DOC_RETRIES).build());
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doNothing().when(vinculationUpdateTwoUseCase).updateAcquisition(anyString());
        doReturn("asd").when(signValidateUseCase).createErrorComplement(any(SDResponseErrorItem.class));
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        signValidateUseCase.actionErrorsRetry(acquisition, errorItem);
        verify(signValidateUseCase, times(1)).actionErrorsRetry(acquisition, errorItem);
    }

    @Test
    public void actionErrorsRetryMax() {
        SDResponseErrorItem errorItem = SDResponseErrorItem.builder().build();
        acquisition.setSignDocRetries(3);
        List<Parameters> parameters = new ArrayList<>();
        parameters.add(Parameters.builder().code("3").parent(PARAMETER_SIGN_DOC_RETRIES).build());
        doReturn(parameters).when(parametersUseCase).findByParent(anyString());
        doNothing().when(signValidateUseCase).updateOperationsStep(any(Acquisition.class));
        doNothing().when(exceptions).createException(any(Map.class), anyString(), anyString(), anyString());
        signValidateUseCase.actionErrorsRetry(acquisition, errorItem);
        verify(signValidateUseCase, times(1)).actionErrorsRetry(acquisition, errorItem);
    }

    @Test
    public void createErrorComplement() {
        SDResponseErrorItem errorItem = SDResponseErrorItem.builder().code("ads").detail("asd").build();
        String s = signValidateUseCase.createErrorComplement(errorItem);
        assertNotNull(s);
    }

    @Test
    public void updateOperationsStep() {
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doNothing().when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        signValidateUseCase.updateOperationsStep(acquisition);
        verify(signValidateUseCase, times(1)).updateOperationsStep(acquisition);
    }
}
