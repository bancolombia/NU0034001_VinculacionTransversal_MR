package co.com.bancolombia.personalinformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.personalinformation.model.PersonalInfoRequest;
import co.com.bancolombia.personalinformation.model.PersonalInfoRequestData;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationStartProcessUseCase;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class PersonalInformationTest {

    private MockMvc mvc;

    private JacksonTester<PersonalInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private PersonalInformationController personalInformationController;

    @Mock
    private PersonalInformationUseCase personalInformationUseCase;

    @Mock
    private PersonalInformationStartProcessUseCase personalInformationStartProcessUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private CoreFunctionString coreFunctionString;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(personalInformationController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(personalInformationController);
    }

    @Test
    @SneakyThrows(Exception.class)
    public void personalInformationTest() {
        UUID uuidCode = UUID.randomUUID();
        PersonalInfoRequestData data = PersonalInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").firstName("Pedro").secondName("Pablo")
                .birthdate("1995-12-18").cellphone("3001239873").email("user@test.com").expeditionCountry("CO")
                .expeditionDate("2020-06-12").expeditionPlace("5001000").firstSurname("Perez").secondSurname("Diaz")
                .build();
        MetaRequest meta = TestUtils.buildMetaRequest("personal-information");

        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(personalInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());

        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/personal-information").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows(Exception.class)
    public void personalInformationEmptyRequestTest() {
        UUID uuidCode = UUID.randomUUID();
        PersonalInfoRequestData data = PersonalInfoRequestData.builder().build();
        MetaRequest meta = TestUtils.buildMetaRequest("personal-information");

        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(personalInfoRequest).getJson();

        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();

        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/personal-information").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows(Exception.class)
    public void personalInformationErrorListRequestTest() {
        UUID uuidCode = UUID.randomUUID();
        PersonalInfoRequestData data = PersonalInfoRequestData.builder().build();
        MetaRequest meta = TestUtils.buildMetaRequest("personal-information");

        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(personalInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/personal-information").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
