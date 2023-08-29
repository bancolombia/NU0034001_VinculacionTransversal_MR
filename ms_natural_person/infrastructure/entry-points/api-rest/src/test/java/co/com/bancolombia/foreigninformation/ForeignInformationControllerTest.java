package co.com.bancolombia.foreigninformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequest;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequestData;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequestDataList;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import co.com.bancolombia.usecase.foreigninformation.ForeignInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ForeignInformationControllerTest {

    private MockMvc mvc;

    private JacksonTester<ForeignCurrencyInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private ForeignInformationController economicInformationController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private CreateModelForeignOperation createModelForeignOperation;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private ForeignInformationUseCase foreignInformationUseCase;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(economicInformationController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(economicInformationController);
    }

    @Test
    public void foreignCurrencyInformationTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").build();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).stateAcquisition(stateAcquisition).build();
        ForeignCurrencyInfoRequestDataList infoRequestDataList = ForeignCurrencyInfoRequestDataList.builder()
                .averageMonthlyAmount("120.00").city("a").country("a").currency("a").department("a").foreignCurrencyTransactionType("a")
                .nameEntity("a").productNumber("1").productType("a").which("a").build();
        ForeignCurrencyInfoRequestData currencyInfoRequestData = ForeignCurrencyInfoRequestData.builder().foreignCurrencyTransactions("S")
                .foreignCurrencyInfoRequestDataList(Collections.singletonList(infoRequestDataList)).acquisitionId(acquisition.getId().toString())
                .documentNumber("111111").documentType("TIPDOC_FS001").build();
        MetaRequest meta = TestUtils.buildMetaRequest("foreign-currency-info");
        ForeignCurrencyInfoRequest foreignCurrencyInfoRequest = new ForeignCurrencyInfoRequest(meta, currencyInfoRequestData);
        String json = jsonUserInfoRequest.write(foreignCurrencyInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(ForeignInformationOperation.builder().build()).when(createModelForeignOperation).creteModelForeignOperation(
                any(ForeignCurrencyInfoRequest.class), any(Acquisition.class));
        Mockito.doReturn(ForeignInformationOperation.builder().build()).when(foreignInformationUseCase).startProcessForeignInformation(any(ForeignInformationOperation.class));

        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/foreign-currency-info")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void foreignCurrencyInformationNTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").build();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).stateAcquisition(stateAcquisition).build();
        ForeignCurrencyInfoRequestDataList infoRequestDataList = ForeignCurrencyInfoRequestDataList.builder()
                .averageMonthlyAmount("120.00").city("a").country("a").currency("a").department("a").foreignCurrencyTransactionType("a")
                .nameEntity("a").productNumber("1").productType("a").which("a").build();
        ForeignCurrencyInfoRequestData currencyInfoRequestData = ForeignCurrencyInfoRequestData.builder().foreignCurrencyTransactions("N")
                .foreignCurrencyInfoRequestDataList(Collections.singletonList(infoRequestDataList)).acquisitionId(acquisition.getId().toString())
                .documentNumber("111111").documentType("TIPDOC_FS001").build();
        MetaRequest meta = TestUtils.buildMetaRequest("foreign-currency-info");
        ForeignCurrencyInfoRequest foreignCurrencyInfoRequest = new ForeignCurrencyInfoRequest(meta, currencyInfoRequestData);
        String json = jsonUserInfoRequest.write(foreignCurrencyInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/foreign-currency-info").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void foreignCurrencyInformationListEmptyTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").build();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).stateAcquisition(stateAcquisition).build();
        ForeignCurrencyInfoRequestDataList infoRequestDataList = ForeignCurrencyInfoRequestDataList.builder().build();
        ForeignCurrencyInfoRequestData currencyInfoRequestData = ForeignCurrencyInfoRequestData.builder().foreignCurrencyTransactions("S")
                .foreignCurrencyInfoRequestDataList(Collections.singletonList(infoRequestDataList)).acquisitionId(acquisition.getId().toString())
                .documentNumber("111111").documentType("TIPDOC_FS001").build();
        MetaRequest meta = TestUtils.buildMetaRequest("foreign-currency-info");
        ForeignCurrencyInfoRequest foreignCurrencyInfoRequest = new ForeignCurrencyInfoRequest(meta, currencyInfoRequestData);
        String json = jsonUserInfoRequest.write(foreignCurrencyInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(ForeignInformationOperation.builder().build()).when(createModelForeignOperation).creteModelForeignOperation(
                any(ForeignCurrencyInfoRequest.class), any(Acquisition.class));
        Mockito.doReturn(ForeignInformationOperation.builder().build()).when(foreignInformationUseCase).startProcessForeignInformation(any(ForeignInformationOperation.class));
        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/foreign-currency-info").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void foreignCurrencyInformationListNullTest() throws Exception {
        UUID uuidCode = UUID.randomUUID();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").build();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).stateAcquisition(stateAcquisition).build();
        ForeignCurrencyInfoRequestData currencyInfoRequestData = ForeignCurrencyInfoRequestData.builder().foreignCurrencyTransactions("S")
                .foreignCurrencyInfoRequestDataList(null).acquisitionId(acquisition.getId().toString())
                .documentNumber("111111").documentType("TIPDOC_FS001").build();
        MetaRequest meta = TestUtils.buildMetaRequest("foreign-currency-info");

        ForeignCurrencyInfoRequest foreignCurrencyInfoRequest = new ForeignCurrencyInfoRequest(meta, currencyInfoRequestData);
        String json = jsonUserInfoRequest.write(foreignCurrencyInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(ForeignInformationOperation.builder().build()).when(createModelForeignOperation).creteModelForeignOperation(
                any(ForeignCurrencyInfoRequest.class), any(Acquisition.class));
        Mockito.doReturn(ForeignInformationOperation.builder().build()).when(foreignInformationUseCase).startProcessForeignInformation(any(ForeignInformationOperation.class));
        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/foreign-currency-info").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}