package co.com.bancolombia.contactinformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.contactinformation.contactinfo.ContactInfoRequest;
import co.com.bancolombia.contactinformation.contactinfo.ContactInfoRequestData;
import co.com.bancolombia.contactinformation.contactinfo.ContactInfoRequestDataAddressList;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCase;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ContactInformationControllerTest {

    private MockMvc mvc;

    private JacksonTester<ContactInfoRequest> jsonContactInfoRequest;

    @InjectMocks
    @Spy
    private ContactInformationController contactInformationController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private CoreFunctionString coreFunctionString;

    @Mock
    private ContactInformationUseCase contactInformationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private GenericStep genericStep;

    @Mock
    private StepForLogFunctional stepForLogFunctional;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(contactInformationController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(contactInformationController);
    }

    @Test
    public void contactInformationSaveListTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        ContactInfoRequestDataAddressList dataList = ContactInfoRequestDataAddressList.builder()
                .addressType("Z001")
                .brand("1")
                .companyName("nombreEmpresa")
                .address("Calle100")
                .neighborhood("CEOHHH")
                .city("5001000")
                .department("05")
                .country("CO")
                .cellphone("3142677025")
                .email("unitTest@gmail.com")
                .phone("2375258")
                .ext("1234").build();

        ContactInfoRequestData data = ContactInfoRequestData.builder().acquisitionId(UUID.randomUUID().toString())
                .documentNumber("1061000000")
                .documentType("TIPDOC_FS001")
                .contactInfoRequestDataAddressListList(Collections.singletonList(dataList)).build();

        MetaRequest meta = TestUtils.buildMetaRequest("contact-information");

        ContactInfoRequest body = new ContactInfoRequest(meta, data);

        doNothing().when(contactInformationController).validateErrorInput(any(ContactInfoRequest.class));

        doReturn(stepForLogFunctional).when(genericStep).firstStepForLogFunctional(data,meta, "CONTINFO");
        doNothing().when(genericStep).validRequest(any(Object.class));

        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisition).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());

        List<ContactInformation> contactInformationList = new ArrayList<>();

        ContactInformation contactInformation = ContactInformation.builder().addressType("Z001")
                .brand("1").companyName("nombre empresa").address("Calle 100").neighborhood("CEOH")
                .city("5001000").department("05").country("CO").cellphone("32144124555").email("unitTest@gmail.com")
                .phone("2375258").ext("1234").build();

        contactInformationList.add(contactInformation);

        //doReturn(contactInformationList).when(contactInformationUseCase)
        //        .startProcessContactInformation(contactInformationList);

        //doReturn(contactInformationList).when(contactInformationUseCase).save(anyList());
        String json = jsonContactInfoRequest.write(body).getJson();

        MockHttpServletResponse response = mvc.perform(post("/natural-person/api/v1/contact-information")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }
}