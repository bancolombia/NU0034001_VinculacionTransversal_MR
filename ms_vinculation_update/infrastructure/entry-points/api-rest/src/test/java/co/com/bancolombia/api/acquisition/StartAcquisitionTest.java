package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCaseImpl;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.model.acquisition.AcquisitionRequest;
import co.com.bancolombia.api.model.acquisition.AcquisitionRequestUser;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.commonsvnt.model.typeperson.TypePerson;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.acquisition.AcquisitionRequestModel;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class StartAcquisitionTest {

    private MockMvc mvc;

    private JacksonTester<AcquisitionRequest> jsonAcquisitionRequest;

    @InjectMocks
    @Spy
    private AcquisitionController acquisitionController;

    @Mock
    private AcquisitionOperationUseCaseImpl acquisitionOperationUseCase;

    @Mock
    private GenericStep genericStep;

    @Mock
    private WebRequest webRequest;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(acquisitionController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(acquisitionController);
    }

    @Test
    public void healthTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                get("/acquisition-update/api/v1/health")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void validStartAcquisitionTest() throws Exception {
        Acquisition acquisitionResponse = Acquisition.builder().id(UUID.randomUUID())
                .typePerson(TypePerson.builder().code("asd").build())
                .typeAcquisition(TypeAcquisition.builder().code("asd").build())
                .stateAcquisition(StateAcquisition.builder().name("asd").build())
                .documentNumber("1061000000").documentType(
                        DocumentType.builder().code("TIPDOC_FS001").build()).typeProduct(
                        TypeProduct.builder().code("1").build()).typeChannel(
                        TypeChannel.builder().code("1").build()).businessLine(
                        BusinessLine.builder().code("1").build()).stateAcquisition(
                        StateAcquisition.builder().code("001").name("Active").build())
                .createdDate(new Date()).build();

        doReturn(acquisitionResponse).when(acquisitionOperationUseCase).startAcquisition(
                any(AcquisitionRequestModel.class), anyString(), anyString());

        AcquisitionRequestUser user = new AcquisitionRequestUser(
                "TIPDOC_FS001", "1061000000", "1", "1", "1");

        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void invalidDocNumDocTypeTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser("DOC", "docTest", "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void invalidDocNumTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser("TIPDOC_FS001", "test",
                "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void invalidLengthDocNumberTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser("DOC",
                "106100000010610000001061000000",
                "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocTypeTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser(null, "1061000000", "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocNumberTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser("TIPDOC_FS001", null, "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void emptyDocNumTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser("TIPDOC_FS001", "", "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void emptyDocTypeTest() throws Exception {
        AcquisitionRequestUser user = new AcquisitionRequestUser("", "1061000000", "1", "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        AcquisitionRequest request = new AcquisitionRequest(user, meta);
        String json = jsonAcquisitionRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-acquisition").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
