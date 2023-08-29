package co.com.bancolombia.economicinformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.economicinformation.model.EconomicInfoRequest;
import co.com.bancolombia.economicinformation.model.EconomicInfoRequestData;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.usecase.economicinformation.EconomicInformationUseCase;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class EconomicInformationControllerTest {

    private MockMvc mvc;

    private JacksonTester<EconomicInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private EconomicInformationController economicInformationController;

    @Mock
    private EconomicInformationUseCase economicInformationUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private CoreFunctionString coreFunctionString;

    @Mock
    private HttpServletRequest request;

    @Mock
    private WebRequest webRequest;

    @Mock
    private Validator validator;

    @Mock
    private StepForLogFunctional stepForLogFunctional;

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
    public void economicInformationTest() throws Exception {
        BigDecimal number = new BigDecimal("1240.25");
        UUID uuidCode = UUID.randomUUID();
        EconomicInfoRequestData data = EconomicInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("1036352115").documentType("TIPDOC_FS001").profession("Z001").positionTrade("Z001").occupation("01")
                .economicActivity("aaa").ciiu("10").monthlyIncome("1240.25").otherMonthlyIncome("1240.25")
                .totalAssets("1240.25").totalLiabilities("1240.25").currency("COP").detailOtherMonthlyIncome("detail").totalMonthlyExpenses("1240.25")
                .annualSales("1240.25").closingDateSales("2020-02-05").patrimony("1240.25").employeesNumber("10")
                .rut("ZS").build();
        MetaRequest meta = TestUtils.buildMetaRequest("economic-information");
        EconomicInfoRequest economicInfoRequest = new EconomicInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(economicInfoRequest).getJson();
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisition).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        EconomicInformation economicInformation = EconomicInformation.builder().acquisition(Acquisition.builder().build())
                .profession("Z001").positionTrade("Z001").occupation("01")
                .economicActivity("aaa").ciiu("10").monthlyIncome(number).otherMonthlyIncome(number)
                .totalAssets(number).totalLiabilities(number).currency("COP").detailOtherMonthlyIncome("detail").totalMonthlyExpenses(number)
                .annualSales(number).closingDateSales(coreFunctionDate.getDatetime()).patrimony(number).employeesNumber("10")
                .rut("ZS").build();
        Mockito.doReturn(economicInformation).when(economicInformationController).constructEconomicInformation(any(EconomicInfoRequest.class), any(Optional.class));
        Mockito.doReturn(EconomicInformation.builder().requiredRut(YES).build()).when(economicInformationUseCase).startProcessEconomicInformation(any(EconomicInformation.class));
        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/economic-information").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void economicInformationNotRutTest() throws Exception {
        BigDecimal number = new BigDecimal("1240.25");
        UUID uuidCode = UUID.randomUUID();
        EconomicInfoRequestData data = EconomicInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("1036352115").documentType("TIPDOC_FS001")
                .ciiu("10").monthlyIncome("1240.25").otherMonthlyIncome("1240.25")
                .totalAssets("1240.25").totalLiabilities("1240.25").currency("COP").detailOtherMonthlyIncome("detail").totalMonthlyExpenses("1240.25")
                .annualSales("1240.25").closingDateSales("2020-02-05").patrimony("1240.25").employeesNumber("10")
                .rut("ZS").build();
        MetaRequest meta = TestUtils.buildMetaRequest("economic-information");
        EconomicInfoRequest economicInfoRequest = new EconomicInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(economicInfoRequest).getJson();
        AcquisitionReply acquisition = AcquisitionReply.builder().acquisitionId(uuidCode.toString())
                .documentNumber("12345").documentType("asd").build();
        doReturn(acquisition).when(vinculationUpdateUseCase)
                .validateAcquisition(anyString(), anyString(), anyString(), anyString());
        EconomicInformation economicInformation = EconomicInformation.builder().acquisition(Acquisition.builder().build())
                .profession("Z001").positionTrade("Z001").occupation("01")
                .economicActivity("aaa").ciiu("10").monthlyIncome(number).otherMonthlyIncome(number)
                .totalAssets(number).totalLiabilities(number).currency("COP").detailOtherMonthlyIncome("detail").totalMonthlyExpenses(number)
                .annualSales(number).closingDateSales(coreFunctionDate.getDatetime()).patrimony(number).employeesNumber("10")
                .rut("ZS").build();
        Mockito.doReturn(economicInformation).when(economicInformationController).constructEconomicInformation(any(EconomicInfoRequest.class), any(Optional.class));
        Mockito.doReturn(EconomicInformation.builder().requiredRut(NOT).build()).when(economicInformationUseCase).startProcessEconomicInformation(any(EconomicInformation.class));
        MockHttpServletResponse response = mvc.perform(
                post("/natural-person/api/v1/economic-information").contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void constructEconomicInformationTest(){
        BigDecimal number = new BigDecimal("1240.25");
        UUID uuidCode = UUID.randomUUID();
        Acquisition acquisition = Acquisition.builder().id(uuidCode).build();
        EconomicInfoRequestData data = EconomicInfoRequestData.builder().acquisitionId(uuidCode.toString())
                .documentNumber("1036352115").documentType("TIPDOC_FS001").profession("Z001").positionTrade("Z001").occupation("01")
                .economicActivity("aaa").ciiu("10").monthlyIncome("1240.25").otherMonthlyIncome("1240.25")
                .totalAssets("1240.25").totalLiabilities("1240.25").currency("COP").detailOtherMonthlyIncome("detail").totalMonthlyExpenses("1240.25")
                .annualSales("1240.25").closingDateSales("2020-02-05").patrimony("1240.25").employeesNumber("10")
                .rut("ZS").build();
        MetaRequest meta = TestUtils.buildMetaRequest("economic-information");
        EconomicInfoRequest economicInfoRequest = new EconomicInfoRequest(meta, data);
        doReturn(new BigDecimal(1)).when(coreFunctionString).stringToDecimal(anyString());
        doReturn(new Date()).when(coreFunctionDate).getDateFromString(anyString(), anyString());
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        EconomicInformation economicInformation = economicInformationController
                .constructEconomicInformation(economicInfoRequest, Optional.of(acquisition));
        assertNotNull(economicInformation);
    }

}