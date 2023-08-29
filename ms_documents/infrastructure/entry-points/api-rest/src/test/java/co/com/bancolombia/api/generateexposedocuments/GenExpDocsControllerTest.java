package co.com.bancolombia.api.generateexposedocuments;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsRequest;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsRequestData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.generateexposedocuments.GenExpDocsUseCase;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.exposedocuments.ExposeDocs;
import co.com.bancolombia.model.generatepdf.GeneratePdf;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
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

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class GenExpDocsControllerTest {

    private MockMvc mvc;

    private JacksonTester<GenExpDocsRequest> jsonRequest;

    @InjectMocks
    @Spy
    private GenExpDocsController genExpDocsController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenExpDocsUseCase genExpDocsUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(genExpDocsController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(genExpDocsController);
    }

    @Test
    public void exposeDocuments() throws Exception {
        UUID uuidCode = UUID.randomUUID();

        GenExpDocsRequestData data = GenExpDocsRequestData.builder()
                .acquisitionId(uuidCode.toString()).formatCode("0")
                .documentNumber("12345").documentType("TIPDOC_FS001").build();

        MetaRequest meta = TestUtils.buildMetaRequest("gen-exp-documents");

        GenExpDocsRequest genExpDocsRequest = new GenExpDocsRequest(meta, data);
        String json = jsonRequest.write(genExpDocsRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder()
                .acquisitionId(uuidCode.toString()).documentNumber("12345")
                .documentType("TIPDOC_FS001").codeTypeAcquisition("VT001").build();

        GeneratePdf generatePdf = GeneratePdf.builder()
                .infoReuseCommon(InfoReuseCommon.builder().mapFields(new HashMap<>()).build()).build();

        ExposeDocs exposeDocs = ExposeDocs.builder().preSignedUrl("asd").build();

        doNothing().when(genericStep).validRequest(any(Object.class));
        doReturn(acquisitionReply).when(vinculationUpdateUseCase).validateAcquisition(
                anyString(), anyString(), anyString(), anyString());
        doReturn(generatePdf).when(genExpDocsUseCase).getPdf(any(Acquisition.class));
        doReturn(exposeDocs).when(genExpDocsUseCase).urlFinal(any(Acquisition.class), any(GeneratePdf.class));

        MockHttpServletResponse response = mvc.perform(
                post("/documents/api/v1/generate-expose-documents")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}