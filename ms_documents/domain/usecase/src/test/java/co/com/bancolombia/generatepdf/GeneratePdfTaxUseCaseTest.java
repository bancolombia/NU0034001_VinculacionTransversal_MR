package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.generatepdf.CompanyInformationPdf;
import co.com.bancolombia.model.generatepdf.ContactInformationPdf;
import co.com.bancolombia.model.generatepdf.EconomicInformationPdf;
import co.com.bancolombia.model.generatepdf.FinancialInformationPdf;
import co.com.bancolombia.model.generatepdf.InternationalOperationPdf;
import co.com.bancolombia.model.generatepdf.TributaryInformationPdf;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GeneratePdfTaxUseCaseTest {

    @InjectMocks
    @Spy
    private GeneratePdfTaxUseCaseImpl generatePdfTaxUseCase;

    @Mock
    private GeneratePdfUtilOneUseCase generatePdfUtilOneUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contactInformationPdfTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(anyString(), any(), anyInt());

        ContactInfoCompReply contactResInfo = ContactInfoCompReply.builder()
                .address("ADDRESS").neighborhood("NEIGHBORHOOD")
                .city("CITY").department("DEPARTMENT").country("COUNTRY")
                .cellphone("3003330033").phone("").email("email@email.com").build();

        ContactInformationPdf contactInformationPdf = generatePdfTaxUseCase.contactInformationPdf(contactResInfo);
        assertNotNull(contactInformationPdf);
    }

    @Test
    public void economicInformationPdfTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getCatalogName(anyString(), anyString());
        doReturn("CODE").when(generatePdfUtilOneUseCase).getCatalogCode(anyString(), anyString());

        EconomicInfoReply economicInfo = EconomicInfoReply.builder()
                .profession("PROFESSION").occupation("OCCUPATION")
                .ciiu("CIIU").employeesNumber("10").build();

        EconomicInformationPdf economicInformationPdf = generatePdfTaxUseCase.economicInformationPdf(economicInfo);
        assertNotNull(economicInformationPdf);
    }

    @Test
    public void companyInformationPdfNullTest() {
        CompanyInformationPdf companyInformationPdf = generatePdfTaxUseCase.companyInformationPdf(null);
        assertNotNull(companyInformationPdf);
    }

    @Test
    public void companyInformationPdfTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(anyString(), any(), anyInt());

        ContactInfoCompReply contactWorkInfo = ContactInfoCompReply.builder()
                .address("ADDRESS").neighborhood("NEIGHBORHOOD")
                .city("CITY").department("DEPARTMENT").country("COUNTRY")
                .cellphone("3003330033").phone("2223344").ext("123").email("email@email.com").build();

        CompanyInformationPdf companyInformationPdf = generatePdfTaxUseCase.companyInformationPdf(contactWorkInfo);
        assertNotNull(companyInformationPdf);
    }

    @Test
    public void financialInformationPdfTest() {
        EconomicInfoReply economicInfo = EconomicInfoReply.builder()
                .monthlyIncome("1000").totalAssets("1000").totalLiabilities("1000")
                .detailOtherMonthlyIncome("1000").totalMonthlyExpenses("10000")
                .annualSales("10000").build();

        FinancialInformationPdf financialInformationPdf = generatePdfTaxUseCase.financialInformationPdf(economicInfo);
        assertNotNull(financialInformationPdf);
    }

    @Test
    public void tributaryInformationPdfTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getCatalogName(anyString(), anyString());
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(anyString(), any(), anyInt());

        List<TaxCountryInfoReply> taxCountries = Arrays.asList(
                TaxCountryInfoReply.builder().country("COUNTRY").taxId("ID").build());

        TaxInfoReply taxInfo = TaxInfoReply.builder()
                .declaringIncome("INCOME").withHoldingAgent("AGENT").vatRegime("VAT")
                .sourceCityResource("CITY").sourceCountryResource("COUNTRY")
                .taxCountryInfoList(taxCountries).build();

        TributaryInformationPdf tributaryInformationPdf = generatePdfTaxUseCase.tributaryInformationPdf(taxInfo);
        assertNotNull(tributaryInformationPdf);
    }

    @Test
    public void tributaryInformationPdfSeveralCountriesTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getCatalogName(anyString(), anyString());
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(anyString(), any(), anyInt());

        List<TaxCountryInfoReply> taxCountries = Arrays.asList(
                TaxCountryInfoReply.builder().country("COUNTRY").taxId("ID").build(),
                TaxCountryInfoReply.builder().country("COUNTRY").taxId("ID").build(),
                TaxCountryInfoReply.builder().country("COUNTRY").taxId("ID").build(),
                TaxCountryInfoReply.builder().country("COUNTRY").taxId("ID").build(),
                TaxCountryInfoReply.builder().country("COUNTRY").taxId("ID").build());

        TaxInfoReply taxInfo = TaxInfoReply.builder()
                .declaringIncome("INCOME").withHoldingAgent("AGENT").vatRegime("VAT")
                .sourceCityResource("CITY").sourceCountryResource("COUNTRY")
                .taxCountryInfoList(taxCountries).build();

        TributaryInformationPdf tributaryInformationPdf = generatePdfTaxUseCase.tributaryInformationPdf(taxInfo);
        assertNotNull(tributaryInformationPdf);
    }

    @Test
    public void internationalOperationPdfTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getCatalogName(anyString(), anyString());
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(anyString(), any(), anyInt());

        List<ForeignCurrencyInfoReply> forCurrencies = Arrays.asList(
                ForeignCurrencyInfoReply.builder()
                        .foreignCurrencyTransactionType("TYPE").city("CITY").country("COUNTRY").build());

        ForeignInfoReply foreignInfo = ForeignInfoReply.builder()
                .foreignCurrencyTransaction("SI").foreignCurrencyInfo(forCurrencies).build();

        InternationalOperationPdf intOperationPdf = generatePdfTaxUseCase.internationalOperationPdf(foreignInfo);
        assertNotNull(intOperationPdf);
    }

    @Test
    public void internationalOperationPdfSeveralCurrenciesTest() {
        doReturn("NAME").when(generatePdfUtilOneUseCase).getCatalogName(anyString(), anyString());
        doReturn("NAME").when(generatePdfUtilOneUseCase).getGeographicName(anyString(), any(), anyInt());

        List<ForeignCurrencyInfoReply> forCurrencies = Arrays.asList(
                ForeignCurrencyInfoReply.builder()
                        .foreignCurrencyTransactionType("TYPE").averageMonthlyAmount("1000")
                        .city("CITY").country("COUNTRY").build(),
                ForeignCurrencyInfoReply.builder()
                        .foreignCurrencyTransactionType("TYPE").averageMonthlyAmount("1000")
                        .city("CITY").country("COUNTRY").build(),
                ForeignCurrencyInfoReply.builder()
                        .foreignCurrencyTransactionType("TYPE").averageMonthlyAmount("1000")
                        .city("CITY").country("COUNTRY").build(),
                ForeignCurrencyInfoReply.builder()
                        .foreignCurrencyTransactionType("TYPE").averageMonthlyAmount("1000")
                        .city("CITY").country("COUNTRY").build());

        ForeignInfoReply foreignInfo = ForeignInfoReply.builder()
                .foreignCurrencyTransaction("SI").foreignCurrencyInfo(forCurrencies).build();

        InternationalOperationPdf intOperationPdf = generatePdfTaxUseCase.internationalOperationPdf(foreignInfo);
        assertNotNull(intOperationPdf);
    }
}
