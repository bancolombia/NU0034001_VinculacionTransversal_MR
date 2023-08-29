package co.com.bancolombia.taxinformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.stateacquisition.StateAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.taxinformation.model.TaxInfoRequest;
import co.com.bancolombia.taxinformation.model.TaxInfoRequestData;
import co.com.bancolombia.taxinformation.model.TaxInfoRequestDataCountryList;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.taxinformation.TaxInformationUseCase;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(MockitoJUnitRunner.class)
public class TaxInformationControllerTest {

    private MockMvc mvc;

    private JacksonTester<TaxInfoRequest> jsonTaxInfoRequest;

    @InjectMocks
    @Spy
    private TaxInformationController taxInformationController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private TaxInformationUseCase taxInformationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private CoreFunctionString coreFunctionString;

    @Mock
    private GenericStep genericStep;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(taxInformationController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(taxInformationController);
    }

    @Test
    @SneakyThrows(Exception.class)
    public void taxInformationTest() {
        UUID uuidCode = UUID.randomUUID();
        TaxInfoRequestDataCountryList countryList = TaxInfoRequestDataCountryList.builder().identifier("1").taxId("12345678").country("UY1").build();
        DocumentType documentType = DocumentType.builder().code("TIPDOC_FS001").build();
        StateAcquisition stateAcquisition = StateAcquisition.builder().code("1").build();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).documentNumber("12345")
                .documentType(documentType).stateAcquisition(stateAcquisition).build();

        TaxInfoRequestData data = TaxInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("TIPDOC_FS001").declaringIncome("S").withHoldingAgent("02")
                .vatRegime("01").originAssetComeFrom("Prueba").sourceCountryResource("CO").sourceCityResource("5001000")
                .requiredToTaxUsTax("1").taxId("12345678").country("CO2").businessTaxPayment("ZS").socialSecurityPayment("ZN")
                .declareTaxInAnotherCountry("1").taxInfoRequestDataCountryList(Collections.singletonList(countryList)).build();

        MetaRequest meta = TestUtils.buildMetaRequest("tax-information");
        TaxInfoRequest taxInfoRequest = new TaxInfoRequest(meta, data);
        String json = jsonTaxInfoRequest.write(taxInfoRequest).getJson();

        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();

        doReturn(acquisitionReply).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        doReturn(TaxInformation.builder().build()).when(taxInformationController)
                .constructTaxInformationObject(any(TaxInfoRequest.class), any(Acquisition.class));

        List<TaxCountry> taxCountryList = Collections.singletonList(TaxCountry.builder().build());
        doReturn(taxCountryList).when(taxInformationController)
                .constructTaxCountry(any(TaxInfoRequest.class), any(Acquisition.class));

        doReturn(TaxInformation.builder().build()).when(taxInformationUseCase)
                .startProcessTaxInformation(any(TaxInformation.class), anyList());

        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/tax-information").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void taxInformationConstructTest() {
        MetaRequest meta = TestUtils.buildMetaRequest("tax-information");
        TaxInfoRequestData data = TaxInfoRequestData.builder().declaringIncome("asd").withHoldingAgent("asd")
                .vatRegime("asd").originAssetComeFrom("asd").sourceCityResource("asd").sourceCountryResource("asd")
                .requiredToTaxUsTax("asd").taxId("asd").country("asd").businessTaxPayment("asd")
                .socialSecurityPayment("asd").declareTaxInAnotherCountry("asd").build();
        TaxInfoRequest taxInfoRequest = new TaxInfoRequest(meta, data);
        doReturn(new Date()).when(coreFunctionDate).getDatetime();

        TaxInformation taxInformationResponse = taxInformationController
                .constructTaxInformationObject(taxInfoRequest, Acquisition.builder().build());
        assertNotNull(taxInformationResponse);
    }

    @Test
    public void constructTaxCountry() {
        MetaRequest meta = TestUtils.buildMetaRequest("tax-information");
        TaxInfoRequestData data = TaxInfoRequestData.builder()
                .taxInfoRequestDataCountryList(
                        Collections.singletonList(TaxInfoRequestDataCountryList.builder()
                                        .identifier("123").taxId("asd").country("asd")
                                .build())).build();
        TaxInfoRequest taxInfoRequest = new TaxInfoRequest(meta, data);
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        doReturn(2).when(coreFunctionString).stringToInteger(anyString());

        List<TaxCountry> taxInformationResponse = taxInformationController
                .constructTaxCountry(taxInfoRequest, Acquisition.builder().build());
        assertNotNull(taxInformationResponse);
    }
}