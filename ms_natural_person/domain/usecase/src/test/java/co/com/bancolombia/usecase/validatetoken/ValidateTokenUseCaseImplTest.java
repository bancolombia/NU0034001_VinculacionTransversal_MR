package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.model.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponseWithLog;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRepository;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRestRepository;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ValidateTokenUseCaseImplTest {

    @InjectMocks
    @Spy
    private ValidateTokenUseCaseImpl validateTokenUseCase;

    @Mock
    private GenerateTokenUseCaseImpl generateTokenUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ValidateTokenValidationUseCase validateTokenValidationUseCase;

    @Mock
    private ValidateTokenRepository validateTokenRepository;

    @Mock
    private ValidateTokenRestRepository validateTokenRestRepository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ValidateTokenCreateRequestUseCase validateTokenCreateRequestUseCase;

    @Mock
    private TokenRetriesUseCaseImpl tokenRetriesUseCase;

    @Mock
    private ValidateTokenMapperUseCaseImpl validateTokenMapperUseCase;

    private Acquisition acquisition;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build())
                .documentType(DocumentType.builder().codeHomologation("asd").build()).build();
    }

    @Test
    public void findByAcquisitionTest() {
        ValidateToken validateToken = ValidateToken.builder().build();
        doReturn(validateToken).when(validateTokenRepository).findByAcquisition(any(Acquisition.class));
        ValidateToken validateToken1 = validateTokenUseCase.findByAcquisition(acquisition);
        assertNotNull(validateToken1);
    }

    @Test
    public void findByAcquisitionLastTest() {
        ValidateToken validateToken = ValidateToken.builder().build();
        doReturn(validateToken).when(validateTokenRepository).findByAcquisitionLast(any(Acquisition.class));
        ValidateToken validateToken1 = validateTokenUseCase.findByAcquisitionLast(acquisition);
        assertNotNull(validateToken1);
    }

    @Test
    public void saveInfoTest() {
        ValidateToken validateToken = ValidateToken.builder().acquisition(acquisition).build();
        doReturn(validateToken).when(validateTokenRepository).save(any(ValidateToken.class));
        ValidateToken validateToken1 = validateTokenUseCase.saveInfo(validateToken);
        assertNotNull(validateToken1);
    }

    @Test
    public void startProcessValidateTokenTest() {
        Date date = new Date();
        Acquisition acquisition1 = Acquisition.builder()
                .documentType(DocumentType.builder().codeGenericType("asd").build())
                .build();

        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("caebarri").build();
        ValidateToken validateToken = ValidateToken.builder().id(UUID.randomUUID()).answerCode("asd")
                .answerDescription("asd").acquisition(acquisition1).tokenCode("123456").build();

        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition1)
                .validateTokenRetries(0).validateTokenLockDate(new Date()).build();

        ValidateTokenResponseWithLog validateTokenResponseWithLog = ValidateTokenResponseWithLog.builder()
                .infoReuseCommon(InfoReuseCommon.builder().build())
                .validateTokenResponse(ValidateTokenResponse.builder().build()).build();

        doReturn(tokenRetries).when(tokenRetriesUseCase).initTokenRetries(any(Acquisition.class), anyString());

        String cellphone = "3009999999";
        ValidateTokenRequest validateTokenRequest = ValidateTokenRequest.builder().build();

        doReturn(cellphone).when(generateTokenUseCase).getCellphoneByLastToken(any(Acquisition.class));
        doReturn("02:00:00").when(validateTokenValidationUseCase).validateRetries
                (any(Acquisition.class), any(TokenRetries.class), anyString());
        doNothing().when(validateTokenValidationUseCase).validateErrors(anyString(), anyString());

        doReturn(validateTokenRequest).when(validateTokenCreateRequestUseCase).createRequest
                (any(Acquisition.class), anyString(), anyString(), anyString());
        doReturn(date).when(coreFunctionDate).getDatetime();
        doReturn(validateTokenResponseWithLog).when(validateTokenRestRepository).getUserInfoFromValidateToken
                (any(ValidateTokenRequest.class), anyString(), any(Date.class));

        doNothing().when(validateTokenValidationUseCase).validateExceptionError(any(ValidateTokenResponse.class));
        doReturn(validateToken).when(validateTokenMapperUseCase).mapperFromValidateToken
                (any(ValidateToken.class), any(ValidateTokenResponse.class), any(BasicAcquisitionRequest.class),
                        anyString());
        doReturn(validateToken).when(validateTokenUseCase).saveInfo(any(ValidateToken.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), any(String.class), anyString());

        ValidateToken validateToken1 = validateTokenUseCase.startProcessValidateToken(
                basicAcquisitionRequest, validateToken);
        assertNotNull(validateToken1);

    }
}