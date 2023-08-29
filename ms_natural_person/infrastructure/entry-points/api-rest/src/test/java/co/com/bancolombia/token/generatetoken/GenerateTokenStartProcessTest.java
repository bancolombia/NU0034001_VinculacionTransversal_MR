package co.com.bancolombia.token.generatetoken;

import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequestData;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;

import java.text.ParseException;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class GenerateTokenStartProcessTest {

    @InjectMocks
    @Spy
    private GenerateTokenStartProcess generateTokenStartProcess;
    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;
    @Mock
    private GenerateTokenUseCase generateTokenUseCase;
    @Mock
    private CoreFunctionString coreFunctionString;

    private Acquisition acquisition;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(generateTokenStartProcess);
    }

    @Test
    public void startProcess() throws ParseException {
        UUID uuidCode = UUID.randomUUID();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("1143360666").documentType("TIPDOC_FS001").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase).validateAcquisition(
                anyString(), anyString(), anyString(), anyString());

        GenerateToken generateToken = GenerateToken.builder()
                .email("asd").cellphone("asd").acquisition(acquisition).createdBy("asd").build();

        doReturn(generateToken).when(generateTokenUseCase).
                startProcessGenerateToken(any(BasicAcquisitionRequest.class), any(GenerateToken.class));

        GenerateTokenRequestData data = GenerateTokenRequestData.builder()
                .acquisitionId("1").documentType("asd").documentNumber("asd").cellphone("asd")
                .email("asd").build();
        MetaRequest meta = MetaRequest.builder().usrMod("asd").build();
        meta.setMessageId("asd");
        doReturn("asd").when(coreFunctionString).lowerCaseString(anyString());

        GenerateToken generateToken1 = generateTokenStartProcess.startProcess(data, meta);
        assertNotNull(generateToken1);
    }
}