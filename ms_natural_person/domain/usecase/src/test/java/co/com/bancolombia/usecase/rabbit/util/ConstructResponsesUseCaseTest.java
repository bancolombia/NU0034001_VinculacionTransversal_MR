package co.com.bancolombia.usecase.rabbit.util;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateTokenReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.BasicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.PersonalInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.taxinformation.TaxInformation;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class ConstructResponsesUseCaseTest {

    @InjectMocks
    @Spy
    private ConstructResponsesUseCase constructResponsesUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fromOriginal() {
        BasicInformation basicInformation = BasicInformation.builder()
                .nationality("a").country("a").birthCity("a").gender("a").civilStatus("a")
                .housingType("a").contractType("a").entryCompanyDate(new Date()).dependants(1)
                .educationLevel("a").socialStratum("a").pep("a").createdDate(new Date()).build();
        BasicInfoReply basicInfoReply = constructResponsesUseCase.fromOriginal(basicInformation);
        assertNotNull(basicInfoReply);
    }

    @Test
    public void fromOriginal2() {
        PersonalInformation inf = PersonalInformation.builder()
                .firstName("a").secondName("a").firstSurname("a")
                .secondSurname("a").birthdate(new Date()).expeditionCountry("a")
                .expeditionPlace("a").expeditionDepartment("a").build();
        PersonalInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal3() {
        ContactInformation inf = ContactInformation.builder()
                .brand("a").addressType("a").address("a").neighborhood("a").country("a").department("a")
                .city("a").email("a").phone("a").ext("a").cellphone("a").build();
        ContactInfoCompReply inf2 = constructResponsesUseCase.fromOriginal(inf);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal4() {
        EconomicInformation inf = EconomicInformation.builder()
                .occupation("a").positionTrade("a").profession("a").monthlyIncome(new BigDecimal(1))
                .detailOtherMonthlyIncome("a").annualSales(new BigDecimal(1)).closingDateSales(new Date())
                .totalAssets(new BigDecimal(1)).totalLiabilities(new BigDecimal(1))
                .totalMonthlyExpenses(new BigDecimal(1)).patrimony(new BigDecimal(1)).ciiu("a")
                .currency("a").employeesNumber("a").rut("a").otherMonthlyIncome(new BigDecimal(1)).build();
        EconomicInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal5() {
        TaxCountry inf = TaxCountry.builder()
                .country("a")
                .taxId("a")
                .build();
        TaxCountryInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal6() {
        List<TaxCountryInfoReply> list = new ArrayList<>();
        TaxInformation inf = TaxInformation.builder()
                .country("a").taxId("a").build();
        TaxInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf, list);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal7() {
        ForeignInformationCurrency inf = ForeignInformationCurrency.builder()
                .foreignCurrencyTransactionType("a").nameEntity("a").productType("a")
                .productNumber("a").averageMonthlyAmount(new BigDecimal(2)).currency("a")
                .country("a").department("a").city("a").build();
        ForeignCurrencyInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal8() {
        List<ForeignCurrencyInfoReply> list = new ArrayList<>();
        ForeignInformation inf = ForeignInformation.builder()
                .foreignCurrencyTransaction("asd").build();
        ForeignInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf, list);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal9() {
        List<ContactInfoCompReply> list = new ArrayList<>();
        ContactInformation inf = ContactInformation.builder()
                .brand("a").addressType("a").address("a").neighborhood("a").country("a").department("a")
                .city("a").email("a").phone("a").ext("a").cellphone("a").build();
        ContactInfoReply inf2 = constructResponsesUseCase.fromOriginal(inf, list);
        assertNotNull(inf2);
    }

    @Test
    public void fromOriginal10() {
        ValidateToken inf = ValidateToken.builder().tokenCode("12332").createdDate(new Date()).build();
        ValidateTokenReply inf2 = constructResponsesUseCase.fromOriginal(inf);
        assertNotNull(inf2);
    }

}