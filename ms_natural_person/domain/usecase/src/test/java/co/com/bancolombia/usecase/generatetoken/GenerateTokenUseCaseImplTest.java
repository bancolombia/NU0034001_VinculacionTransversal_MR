package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRepository;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRestRepository;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseOk;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.model.tokenretries.TokenRetries;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseImpl;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GenerateTokenUseCaseImplTest {

    @InjectMocks
    @Spy
    private GenerateTokenUseCaseImpl generateTokenUseCase;

    @Mock
    private VinculationUpdateUseCaseImpl vinculationUpdateUseCase;

    @Mock
    private GenerateTokenRepository generateTokenRepository;

    @Mock
    private GenerateTokenRestRepository generateTokenRestRepository;

    @Mock
    private TokenRetriesUseCaseImpl tokenRetriesUseCase;

    @Mock
    private GenerateTokenValidationUseCaseImpl generateTokenValidationUseCase;

    @Mock
    private GenerateTokenConstructUseCaseImpl generateTokenConstructUseCase;

    @Mock
    private PersonalInformationRepository personalInformationRepository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

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
    public void saveTest() {
        GenerateToken generateToken = GenerateToken.builder().acquisition(acquisition).build();
        doReturn(generateToken).when(this.generateTokenRepository).save(any(GenerateToken.class));
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().requestReuse("json").responseReuse("json")
                .build();
        GenerateToken gt = this.generateTokenUseCase.save(generateToken, infoReuseCommon);
        assertEquals(generateToken.getAcquisition().getId().toString(),
                gt.getAcquisition().getId().toString());
    }

    @Test
    public void startProcessGenerateTokenTest() throws ParseException {
        Acquisition acquisition1 = Acquisition.builder()
                .documentType(DocumentType.builder().code("123").codeGenericType("asd").build())
                .build();
        GenerateToken generateToken = GenerateToken.builder()
                .acquisition(acquisition1).email("email@email.com").cellphone("31056684852").build();
        GTRequest generateTokenRequest = GTRequest.builder().build();
        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .documentNumber("123").messageId("asd").userTransaction("juandiego").build();
        GTResponseWithLog responseWithLog = GTResponseWithLog.builder()
                .infoReuseCommon(InfoReuseCommon.builder().build())
                .generateTokenResponse(GTResponseOk.builder().build()).build();

        TokenRetries tokenRetries = TokenRetries.builder().id(UUID.randomUUID()).acquisition(acquisition1)
                .generateTokenRetries(0).generateTokenLockDate(new Date()).build();

        doReturn(tokenRetries).when(tokenRetriesUseCase).initTokenRetries(any(Acquisition.class), anyString());
        doReturn("02:00:00").when(generateTokenValidationUseCase).validateRetries
                (any(Acquisition.class), any(TokenRetries.class), anyString());
        doReturn(generateToken).when(generateTokenUseCase).cellAndEmail(any(GenerateToken.class));
        doNothing().when(generateTokenValidationUseCase).validateGenerateToken(any(GenerateToken.class), anyString());
        doReturn(generateTokenRequest).when(generateTokenConstructUseCase).createRequestGetToken
                (any(GenerateToken.class), anyString(), anyString());
        doReturn(date).when(coreFunctionDate).getDatetime();
        doReturn(responseWithLog).when(generateTokenRestRepository).getToken(
                any(GTRequest.class), anyString(), any(Date.class));
        doReturn(generateToken).when(generateTokenConstructUseCase).reFormatGenerateToken(any(GenerateToken.class),
                any(BasicAcquisitionRequest.class), any(GTResponseWithLog.class));
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), any(String.class), anyString());
        doReturn(generateToken).when(generateTokenUseCase).save(
                any(GenerateToken.class), any(InfoReuseCommon.class));

        GenerateToken generateToken1 = generateTokenUseCase.startProcessGenerateToken(basicAcquisitionRequest,
                generateToken);
        assertNotNull(generateToken1);
    }


    @Test
    public void cellAndEmailTest(){
        GenerateToken generateToken = GenerateToken.builder().acquisition(acquisition).build();

        PersonalInformation personalInformation = PersonalInformation.builder()
                .acquisition(acquisition).email("email@gmail.com").cellphone("3193472013").build();
        Mockito.doReturn(personalInformation).when(personalInformationRepository).findByAcquisition(acquisition);
        GenerateToken generateToken1 = generateTokenUseCase.cellAndEmail(generateToken);
        assertNotNull(generateToken1);
    }


    @Test
    public void getCellphoneByLastTokenTest(){
        GenerateToken generateToken = GenerateToken.builder().cellphone("3193472013").build();
        Mockito.doReturn(generateToken).when(generateTokenRepository)
                .findTopByAcquisitionOrderByCreatedDateDesc(acquisition);
        String cell = generateTokenUseCase.getCellphoneByLastToken(acquisition);
        assertNotNull(cell);
    }
}
