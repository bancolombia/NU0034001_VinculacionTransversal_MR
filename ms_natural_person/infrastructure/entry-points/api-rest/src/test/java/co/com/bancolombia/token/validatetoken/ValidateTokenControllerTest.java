package co.com.bancolombia.token.validatetoken;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequestData;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenUseCase;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ValidateTokenControllerTest {
    private MockMvc mvc;

    private JacksonTester<ValidateTokenRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private ValidateTokenController validateTokenController;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ValidateTokenUseCase validateTokenUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private GenericStep genericStep;

    private Acquisition acquisition;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(validateTokenController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(validateTokenController);
    }

    @Test
    public void validateTokenControllerTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        ValidateTokenRequestData data = ValidateTokenRequestData.builder()
                .tokenCode("123456")
                .acquisitionId(uuidCode.toString())
                .documentNumber("12345")
                .documentType("TIPDOC_FS001")
                .build();
        MetaRequest meta = TestUtils.buildMetaRequest("validate-token");

        ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest(meta, data);
        String json = jsonUserInfoRequest.write(validateTokenRequest).getJson();

        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(DocumentType.builder().build()).build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().dateRequestReuse(new Date())
                .dateResponseReuse(new Date()).requestReuse("asd").responseReuse("asdf").build();
        ValidateToken validateToken = ValidateToken.builder().acquisition(acquisition).infoReuseCommon(infoReuseCommon)
                .id(acquisition.getId()).build();

        doReturn(validateToken).when(validateTokenController)
                .startProcess(any(ValidateTokenRequestData.class), any(MetaRequest.class));

        MockHttpServletResponse response = mvc.perform(post("/natural-person/api/v1/validate-token")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void startProcess() {
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("1143360666").documentType("TIPDOC_FS001").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase).validateAcquisition(
                anyString(), anyString(), anyString(), anyString());

        ValidateToken validateToken = ValidateToken.builder()
                .tokenCode("123456").acquisition(acquisition).createdBy("asd").build();

        doReturn(validateToken).when(validateTokenUseCase).startProcessValidateToken
                (any(BasicAcquisitionRequest.class), any(ValidateToken.class));

        ValidateTokenRequestData data = ValidateTokenRequestData.builder()
                .acquisitionId("1").documentType("asd").documentNumber("asd").tokenCode("123456")
                .build();
        MetaRequest meta = MetaRequest.builder().usrMod("asd").build();
        meta.setMessageId("asd");

        ValidateToken validateToken1 = validateTokenController.startProcess(data, meta);
        assertNotNull(validateToken1);
    }
}