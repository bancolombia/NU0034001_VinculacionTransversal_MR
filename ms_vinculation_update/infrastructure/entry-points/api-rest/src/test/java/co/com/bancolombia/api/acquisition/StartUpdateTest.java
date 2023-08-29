package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCaseImpl;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.model.acquisition.UpdateRequest;
import co.com.bancolombia.api.model.acquisition.UpdateRequestUser;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.businessline.BusinessLine;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.model.typechannel.TypeChannel;
import co.com.bancolombia.commonsvnt.model.typeproduct.TypeProduct;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class StartUpdateTest {

    private MockMvc mvc;

    private JacksonTester<UpdateRequest> jsonUpdateRequest;

    @InjectMocks
    @Spy
    private UpdateController updateController;

    @Mock
    private AcquisitionOperationUseCaseImpl acquisitionOperationUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(updateController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(updateController);
    }

    @Test
    public void validStartUpdateTest() throws Exception {
        AcquisitionRequestModel requestModel = AcquisitionRequestModel.builder()
                .documentType("TIPDOC_FS003")
                .documentNumber("900185070")
                .typePerson(null)
                .typeProduct(null)
                .typeChannel("4")
                .businessLine("1")
                .token("123456789").build();

        Acquisition acquisitionResponse = Acquisition.builder().id(UUID.randomUUID())
                .documentNumber("900185070").documentType(
                        DocumentType.builder().code("TIPDOC_FS003").build()).typeProduct(
                        TypeProduct.builder().code(null).build()).typeChannel(
                        TypeChannel.builder().code("4").build()).businessLine(
                        BusinessLine.builder().code("1").build()).stateAcquisition(
                        StateAcquisition.builder().code("001").name("Active").build())
                .createdDate(new Date()).build();

        when(acquisitionOperationUseCase.startAcquisition(requestModel, "" +
                "BIZAGI", Constants.CODE_START_UPDATE)).thenReturn(acquisitionResponse);

        UpdateRequestUser user = new UpdateRequestUser("TIPDOC_FS003", "900185070",
                "4", "1", "123456789");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void invalidDocNumDocTypeTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser("DOC", "docTest", "1",
                "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void invalidDocNumTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser("TIPDOC_FS003",
                "12345678901234567890123", "4", "1", "123456789");

        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void invalidLengthDocNumberTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser("TIPDOC_FS003",
                "106100000010610000001061000000",
                "4", "1", "123456");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);

        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocTypeTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser(null, "900185170", "4",
                "1", "12345");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocNumberTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser("TIPDOC_FS003", null, "4",
                "1", "123456");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void emptyDocNumTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser("TIPDOC_FS003", "", "1",
                "1", "1");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void emptyDocTypeTest() throws Exception {
        UpdateRequestUser user = new UpdateRequestUser("", "900185170", "4",
                "1", "123456");
        MetaRequest meta = TestUtils.buildMetaRequest("start-update");

        UpdateRequest request = new UpdateRequest(user, meta);
        String json = jsonUpdateRequest.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/start-update").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
