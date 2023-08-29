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

import java.util.List;

@RequiredArgsConstructor
public class ConstructResponsesUseCase {

    public BasicInfoReply fromOriginal(BasicInformation inf){
        return BasicInfoReply.builder()
                .nationality(inf.getNationality())
                .country(inf.getCountry())
                .birthCity(inf.getBirthCity())
                .birthDepartment(inf.getBirthDepartment())
                .gender(inf.getGender())
                .civilStatus(inf.getCivilStatus())
                .housingType(inf.getHousingType())
                .contractType(inf.getContractType())
                .entryCompanyDate(inf.getEntryCompanyDate())
                .dependants(inf.getDependants().toString())
                .educationLevel(inf.getEducationLevel())
                .socialStratum(inf.getSocialStratum())
                .pep(inf.getPep())
                .createdDate(inf.getCreatedDate())
                .build();
    }

    public PersonalInfoReply fromOriginal(PersonalInformation inf){
        return PersonalInfoReply.builder()
                .firstName(inf.getFirstName())
                .secondName(inf.getSecondName())
                .firstSurname(inf.getFirstSurname())
                .secondSurname(inf.getSecondSurname())
                .birthdate(inf.getBirthdate())
                .expeditionCountry(inf.getExpeditionCountry())
                .expeditionPlace(inf.getExpeditionPlace())
                .expeditionDate(inf.getExpeditionDate())
                .expeditionDepartment(inf.getExpeditionDepartment())
                .cellphone(inf.getCellphone())
                .email(inf.getEmail())
                .createdDate(inf.getCreatedDate())
                .build();
    }

    public ContactInfoCompReply fromOriginal(ContactInformation inf){
        return ContactInfoCompReply.builder()
                .brand(inf.getBrand())
                .addressType(inf.getAddressType())
                .address(inf.getAddress())
                .neighborhood(inf.getNeighborhood())
                .country(inf.getCountry())
                .department(inf.getDepartment())
                .city(inf.getCity())
                .email(inf.getEmail())
                .phone(inf.getPhone())
                .ext(inf.getExt())
                .cellphone(inf.getCellphone())
                .companyName(inf.getCompanyName())
                .build();
    }

    public ContactInfoReply fromOriginal(ContactInformation inf, List<ContactInfoCompReply> list) {
        return ContactInfoReply.builder()
                .createdDate(inf.getCreatedDate())
                .contactInfoCompList(list)
                .build();
    }

    public EconomicInfoReply fromOriginal(EconomicInformation inf){
        return EconomicInfoReply.builder()
                .occupation(inf.getOccupation())
                .positionTrade(inf.getPositionTrade())
                .profession(inf.getProfession())
                .monthlyIncome(inf.getMonthlyIncome().toString())
                .detailOtherMonthlyIncome(inf.getDetailOtherMonthlyIncome())
                .annualSales(inf.getAnnualSales().toString())
                .closingDateSales(inf.getClosingDateSales())
                .totalAssets(inf.getTotalAssets().toString())
                .totalLiabilities(inf.getTotalLiabilities().toString())
                .totalMonthlyExpenses(inf.getTotalMonthlyExpenses().toString())
                .patrimony(inf.getPatrimony().toString())
                .ciiu(inf.getCiiu())
                .currency(inf.getCurrency())
                .employeesNumber(inf.getEmployeesNumber())
                .rut(inf.getRut())
                .economicActivity(inf.getEconomicActivity())
                .otherMonthlyIncome(inf.getOtherMonthlyIncome().toString())
                .createdDate(inf.getCreatedDate())
                .build();
    }

    public TaxCountryInfoReply fromOriginal(TaxCountry inf){
        return TaxCountryInfoReply.builder()
                .country(inf.getCountry())
                .taxId(inf.getTaxId())
                .build();
    }

    public TaxInfoReply fromOriginal(TaxInformation inf, List<TaxCountryInfoReply> list){
        return TaxInfoReply.builder()
                .country(inf.getCountry())
                .taxId(inf.getTaxId())
                .sourceCountryResource(inf.getSourceCountryResource())
                .sourceCityResource(inf.getSourceCityResource())
                .declaringIncome(inf.getDeclaringIncome())
                .vatRegime(inf.getVatRegime())
                .withHoldingAgent(inf.getWithHoldingAgent())
                .businessTaxPayment(inf.getBusinessTaxPayment())
                .socialSecurityPayment(inf.getSocialSecurityPayment())
                .originAssetComeFrom(inf.getOriginAssetComeFrom())
                .declareTaxInAnotherCountry(inf.getDeclareTaxInAnotherCountry())
                .createdDate(inf.getCreatedDate())
                .taxCountryInfoList(list)
                .build();
    }

    public ForeignCurrencyInfoReply fromOriginal(ForeignInformationCurrency info){
        return ForeignCurrencyInfoReply.builder()
                .foreignCurrencyTransactionType(info.getForeignCurrencyTransactionType())
                .nameEntity(info.getNameEntity())
                .productType(info.getProductType())
                .productNumber(info.getProductNumber())
                .averageMonthlyAmount(info.getAverageMonthlyAmount().toString())
                .currency(info.getCurrency())
                .country(info.getCountry())
                .department(info.getDepartment())
                .city(info.getCity())
                .which(info.getWhich())
                .build();
    }

    public ForeignInfoReply fromOriginal(ForeignInformation info, List<ForeignCurrencyInfoReply> list){
        return ForeignInfoReply.builder()
                .foreignCurrencyTransaction(info.getForeignCurrencyTransaction())
                .createdDate(info.getCreatedDate())
                .foreignCurrencyInfo(list)
                .build();
    }

    public ValidateTokenReply fromOriginal(ValidateToken info) {
        return ValidateTokenReply.builder()
                .tokenCode(info.getTokenCode())
                .createdDate(info.getCreatedDate())
                .build();
    }
}