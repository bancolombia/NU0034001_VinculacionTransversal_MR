package co.com.bancolombia.token;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequest;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequestData;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenResponse;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequestData;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ResponseFactoryTokenTest {

    private MockMvc mvc;

    @InjectMocks
    @Spy
    private ResponseFactoryToken responseFactoryToken;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(responseFactoryToken)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(responseFactoryToken);
    }

    @Test
    public void buildGenerateTokenResponseTest() {
        UUID uuidCode = UUID.randomUUID();
        GenerateTokenRequestData data = GenerateTokenRequestData.builder()
                .cellphone("3006633887")
                .email("cb@test.com")
                .acquisitionId(uuidCode.toString())
                .documentNumber("12345")
                .documentType("TIPDOC_FS001")
                .build();
        MetaRequest meta = TestUtils.buildMetaRequest("generate-token");
        GenerateTokenRequest generateTokenRequest = new GenerateTokenRequest(meta, data);

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(DocumentType.builder().build()).build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).requestReuse("asd").responseReuse("asdf").build();
        GenerateToken generateToken = GenerateToken.builder().acquisition(acquisition).infoReuseCommon(infoReuseCommon)
                .id(acquisition.getId()).build();


        GenerateTokenResponse generateTokenResponse = ResponseFactoryToken.buildGenerateTokenResponse(generateTokenRequest, generateToken);
        assertNotNull(generateTokenResponse);
    }

    @Test
    public void buildValidateTokenResponseTest() {
        UUID uuidCode = UUID.randomUUID();
        ValidateTokenRequestData data = ValidateTokenRequestData.builder()
                .tokenCode("123456")
                .acquisitionId(uuidCode.toString())
                .documentNumber("12345")
                .documentType("TIPDOC_FS001")
                .build();
        MetaRequest meta = TestUtils.buildMetaRequest("validate-token");
        ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest(meta, data);

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(DocumentType.builder().build()).build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).requestReuse("asd").responseReuse("asdf").build();
        ValidateToken validateToken = ValidateToken.builder().acquisition(acquisition).infoReuseCommon(infoReuseCommon)
                .id(acquisition.getId()).build();

        ValidateTokenResponse validateTokenResponse = ResponseFactoryToken.buildValidateTokenResponse(validateTokenRequest, validateToken);
        assertNotNull(validateTokenResponse);
    }
}