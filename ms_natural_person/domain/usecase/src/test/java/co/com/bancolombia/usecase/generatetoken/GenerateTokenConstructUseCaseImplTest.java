package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.generatetoken.reuserequest.GTAccountInformation;
import co.com.bancolombia.model.generatetoken.reuserequest.GTCellphoneInformation;
import co.com.bancolombia.model.generatetoken.reuserequest.GTCustomerIdentification;
import co.com.bancolombia.model.generatetoken.reuserequest.GTEmailInformation;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequestItem;
import co.com.bancolombia.model.generatetoken.reuserequest.GTTechTokenInfo;
import co.com.bancolombia.model.generatetoken.reuserequest.GTTechincalTokenInformation;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTDatum;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponse;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseOk;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class GenerateTokenConstructUseCaseImplTest {

    @InjectMocks
    @Spy
    private GenerateTokenConstructUseCaseImpl generateTokenConstructUseCase;

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
    public void reFormatGenerateTokenTest() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new CoreFunctionDate().getDatetime();

        BasicAcquisitionRequest basicAcquisitionRequest = BasicAcquisitionRequest.builder()
                .messageId("messageId").build();
        GTResponse response = GTResponse.builder().answerCode("answerCode").answerDescription("answerName")
                .responseDate(df.format(now)).build();
        GTDatum datum = GTDatum.builder()
                .generateTokenResponse(response).build();
        GTResponseOk generateTokenResponse = GTResponseOk.builder()
                .data(datum).build();
        GTResponseWithLog generateTokenResponseWithLog = GTResponseWithLog.builder()
                .generateTokenResponse(generateTokenResponse).build();
        GenerateToken generateTokenEx = generateTokenConstructUseCase.reFormatGenerateToken(
                GenerateToken.builder().build(), basicAcquisitionRequest, generateTokenResponseWithLog);
        assertNotNull(generateTokenEx);
    }

    @Test
    public void createRequestGetTokenTest() {
        Acquisition acquisition1 = Acquisition.builder()
                .documentType(DocumentType.builder().code("123").codeGenericType("asd").build())
                .build();
        GenerateToken generateToken = GenerateToken.builder()
                .acquisition(acquisition1).email("email@email.com").cellphone("31056684852").build();

        GTTechTokenInfo techTokenInfo = GTTechTokenInfo.builder()
                .generateEncryptedToken("0").tokenValidity("0000").build();

        GTTechincalTokenInformation technicalTokenInformation = GTTechincalTokenInformation.builder()
                .sendToken("3")
                .serverId("asd")
                .sourceSystemId("asd")
                .techTokenInfo(techTokenInfo).build();

        GTCustomerIdentification customerIdentification = GTCustomerIdentification.builder()
                .documentNumber("123").documentType("asd").build();

        GTAccountInformation accountInformation = GTAccountInformation.builder()
                .accountNumber("0").accountType("1").build();

        GTCellphoneInformation cellphoneInformation = GTCellphoneInformation.builder()
                .cellphoneNumber("1234").build();

        GTEmailInformation emailInformation = GTEmailInformation.builder()
                .email("asd@test.com").alertTemplateCode("0000").build();

        GTRequestItem requestItem = GTRequestItem.builder()
                .techincalTokenInformation(technicalTokenInformation)
                .customerIdentification(customerIdentification)
                .accountInformation(accountInformation)
                .cellphoneInformation(cellphoneInformation)
                .emailInformation(emailInformation).build();

        GTRequest.builder().data(requestItem).build();

        GTRequest generateTokenConstruct = generateTokenConstructUseCase.createRequestGetToken(generateToken, "123", "asd");
        assertNotNull(generateTokenConstruct);
    }
}