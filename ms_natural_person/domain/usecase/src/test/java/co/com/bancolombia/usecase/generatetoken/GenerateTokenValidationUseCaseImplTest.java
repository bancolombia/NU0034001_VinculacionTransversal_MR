package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCaseImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

public class GenerateTokenValidationUseCaseImplTest {

    @InjectMocks
    @Spy
    private GenerateTokenValidationUseCaseImpl generateTokenValidationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private Exceptions exceptions;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ParametersRepository parametersRepository;

    @Mock
    private TokenRetriesUseCaseImpl tokenRetriesUseCase;

    private Acquisition acquisition;
    private Date date;

    @Before
    public void setUp() {
        date = new Date();
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build())
                .build();
    }

    @Test
    public void validateRetriesTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .generateTokenRetries(0).generateTokenLockDate(new Date()).build();
        Parameters par = Parameters.builder().code("3").build();
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("juandiego").build();
        doReturn(Collections.singletonList(par)).when(parametersRepository).findByParent(anyString());
        doReturn(tokenRetries).when(tokenRetriesUseCase).save(any(TokenRetries.class));
        String msj = generateTokenValidationUseCase.validateRetries(acquisition, tokenRetries, basicAcquisitionRequest.getUserTransaction());
        assertNull(msj);
    }

    @Test
    public void validateRetriesCheckListBlockTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .generateTokenRetries(4).generateTokenLockDate(new Date()).build();
        Parameters par = Parameters.builder().code("3").build();
        doReturn(Collections.singletonList(par)).when(parametersRepository).findByParent(anyString());

        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("juandiego").build();

        ChecklistReply checkList = ChecklistReply.builder()
                .acquisitionId(acquisition.getId().toString()).stateOperation(CODE_ST_OPE_BLOQUEADO).build();
        doReturn(checkList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());

        doReturn("02:00:00").when(generateTokenValidationUseCase).tokenGenerationUnBlock(
                any(Acquisition.class), anyString(), any(TokenRetries.class));
        doReturn(tokenRetries).when(tokenRetriesUseCase).save(any(TokenRetries.class));
        String msj = generateTokenValidationUseCase.validateRetries(acquisition, tokenRetries, basicAcquisitionRequest.getUserTransaction());
        assertNotNull(msj);
    }

    @Test
    public void validateRetriesCheckListBlockTest2(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .generateTokenRetries(4).generateTokenLockDate(new Date()).build();
        Parameters par = Parameters.builder().code("3").build();
        doReturn(Collections.singletonList(par)).when(parametersRepository).findByParent(anyString());

        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("juandiego").build();

        ChecklistReply checkList = ChecklistReply.builder()
                .acquisitionId(acquisition.getId().toString()).stateOperation(CODE_ST_OPE_COMPLETADO).build();
        doReturn(checkList).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString());

        doReturn("asd").when(coreFunctionDate).minutesFormat(anyInt());
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doReturn(new Date()).when(coreFunctionDate).getDatetime();

        doReturn(tokenRetries).when(tokenRetriesUseCase).save(any(TokenRetries.class));
        String msj = generateTokenValidationUseCase.validateRetries(acquisition, tokenRetries, basicAcquisitionRequest.getUserTransaction());
        assertNotNull(msj);
    }

    @Test
    public void tokenGenerationUnBlockTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .generateTokenRetries(2).generateTokenLockDate(new Date()).build();

        Mockito.doReturn("02:59:00").when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));
        String difference = generateTokenValidationUseCase.tokenGenerationUnBlock(acquisition, "180", tokenRetries);
        assertNotNull(difference);
    }

    @Test
    public void tokenGenerationUnBlockDifferenceNTest(){
        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition)
                .generateTokenRetries(2).generateTokenLockDate(new Date()).build();

        doReturn(null).when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));
        String difference = generateTokenValidationUseCase.tokenGenerationUnBlock(acquisition, "0", tokenRetries);
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        assertNull(difference);
    }

    @Test
    public void validateGenerateTokenMsjNNTest(){
        GenerateToken generateToken = GenerateToken.builder().build();
        doNothing().when(exceptions).createException(any(HashMap.class), anyString(), anyString(), anyString());
        generateTokenValidationUseCase.validateGenerateToken(generateToken, "asd");
    }

    @Test
    public void validateGenerateTokenEmailCellphoneNTest(){
        GenerateToken generateToken = GenerateToken.builder().build();
        doNothing().when(exceptions).createException(any(HashMap.class), anyString(), anyString(), anyString());
        generateTokenValidationUseCase.validateGenerateToken(generateToken, null);
    }

    @Test
    public void validateGenerateTokenEmailCellphoneNNTest(){
        GenerateToken generateToken = GenerateToken.builder().cellphone("310").email("a@g.com").build();
        generateTokenValidationUseCase.validateGenerateToken(generateToken, null);
    }
}