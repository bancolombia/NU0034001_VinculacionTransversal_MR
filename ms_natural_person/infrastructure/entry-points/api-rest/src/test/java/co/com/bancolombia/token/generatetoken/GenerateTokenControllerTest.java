package co.com.bancolombia.token.generatetoken;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequest;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class GenerateTokenControllerTest {

    private MockMvc mvc;

    private JacksonTester<GenerateTokenRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private GenerateTokenController generateTokenController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private GenerateTokenStartProcess generateTokenStartProcess;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(generateTokenController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(generateTokenController);
    }

    @Test
    public void generateTokenControllerTest() throws Exception {
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
        String json = jsonUserInfoRequest.write(generateTokenRequest).getJson();

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(DocumentType.builder().build()).build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).requestReuse("asd").responseReuse("asdf").build();
        GenerateToken generateToken = GenerateToken.builder().acquisition(acquisition).infoReuseCommon(infoReuseCommon)
                .id(acquisition.getId()).build();

        doReturn(generateToken).when(generateTokenStartProcess)
                .startProcess(any(GenerateTokenRequestData.class), any(MetaRequest.class));

        MockHttpServletResponse response = mvc.perform(post("/natural-person/api/v1/generate-token")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}