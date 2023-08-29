package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.model.validatetoken.ErrorItem;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCaseImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_BLOQUEADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ValidateTokenValidationUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateTokenValidationUseCase validateTokenValidationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    ParametersUseCase parametersUseCase;

    @Mock
    private TokenRetriesUseCaseImpl tokenRetriesUseCase;

    @Mock
    private Exceptions exceptions;



    private Acquisition acquisition;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build())
                .documentType(DocumentType.builder().codeHomologation("asd").build()).build();
    }

    @Test
    public void validateRetriesTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .validateTokenRetries(1).validateTokenLockDate(new Date()).build();
        Parameters par = Parameters.builder().code("3").build();
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("caebarri").build();


        doReturn(Arrays.asList(par)).when(parametersUseCase).findByParent(anyString());
        doReturn(tokenRetries).when(tokenRetriesUseCase).save(any(TokenRetries.class));
        String msj = validateTokenValidationUseCase.validateRetries(acquisition, tokenRetries, basicAcquisitionRequest.getUserTransaction());
        assertNull(msj);
    }

    @Test
    public void validateRetriesCheckListBlockTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .validateTokenRetries(4).validateTokenLockDate(new Date()).build();
        Parameters par = Parameters.builder().code("3").build();
        doReturn(Arrays.asList(par)).when(parametersUseCase).findByParent(anyString());
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("caebarri").build();

        ChecklistReply checkList = ChecklistReply.builder()
                .acquisitionId(acquisition.getId().toString()).stateOperation(CODE_ST_OPE_BLOQUEADO).build();
        doReturn(checkList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());

        doReturn("02:00:00").when(validateTokenValidationUseCase).validateTokenUnBlock(
                any(Acquisition.class), anyString(), any(TokenRetries.class));
        doReturn    (tokenRetries).when(tokenRetriesUseCase).save(any(TokenRetries.class));
        String msj = validateTokenValidationUseCase.validateRetries(
                acquisition, tokenRetries, basicAcquisitionRequest.getUserTransaction());
        assertNotNull(msj);
    }

    @Test
    public void validateRetriesCheckListBlockTest2(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .validateTokenRetries(4).validateTokenLockDate(new Date()).build();
        Parameters par = Parameters.builder().code("3").build();
        doReturn(Arrays.asList(par)).when(parametersUseCase).findByParent(anyString());
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("caebarri").build();

        ChecklistReply checkList = ChecklistReply.builder()
                .acquisitionId(acquisition.getId().toString()).stateOperation(CODE_ST_OPE_COMPLETADO).build();
        doReturn(checkList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());

        doReturn("asd").when(coreFunctionDate).minutesFormat(anyInt());
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        doReturn(tokenRetries).when(tokenRetriesUseCase).save(any(TokenRetries.class));
        String msj = validateTokenValidationUseCase.validateRetries(
                acquisition, tokenRetries, basicAcquisitionRequest.getUserTransaction());
        assertNotNull(msj);
    }

    @Test
    public void validateTokenUnBlockTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .validateTokenRetries(2).validateTokenLockDate(new Date()).build();
        doReturn(null).when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));
        String difference = validateTokenValidationUseCase.validateTokenUnBlock(acquisition, "1", tokenRetries);
        assertNull(difference);
    }

    @Test(expected = ValidationException.class)
    public void validateTokenUnBlockDiffTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .validateTokenRetries(2).validateTokenLockDate(new Date()).build();
        doReturn("02:59:00").when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));
        validateTokenValidationUseCase.validateTokenUnBlock(acquisition, "180", tokenRetries);

    }



    @Test(expected = ValidationException.class)
    public void validateErrorsMsjTest(){
        validateTokenValidationUseCase.validateErrors(null, "block");
    }

    @Test(expected = ValidationException.class)
    public void validateErrorsCellphoneTest(){
        validateTokenValidationUseCase.validateErrors(null, null);
    }

    @Test
    public void validateExceptionErrorTest() {
        ErrorItem errorItem = ErrorItem.builder().detail("asd").build();
        List<ErrorItem> errorItems = new ArrayList<>();
        errorItems.add(errorItem);
        ValidateTokenResponse validateTokenResponse = ValidateTokenResponse.builder().errors(errorItems).build();
        doNothing().when(exceptions).createException(any(HashMap.class), anyString(), anyString(), anyString());
        validateTokenValidationUseCase.validateExceptionError(validateTokenResponse);
        verify(this.validateTokenValidationUseCase, times(1)).validateExceptionError(validateTokenResponse);

    }

}