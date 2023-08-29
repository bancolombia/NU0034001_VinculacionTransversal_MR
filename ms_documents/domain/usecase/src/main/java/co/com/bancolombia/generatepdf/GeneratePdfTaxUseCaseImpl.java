package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ContactInfoCompReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.EconomicInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignCurrencyInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.ForeignInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxCountryInfoReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.reply.TaxInfoReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.generatepdf.CompanyInformationPdf;
import co.com.bancolombia.model.generatepdf.ContactInformationPdf;
import co.com.bancolombia.model.generatepdf.CountryTaxPdf;
import co.com.bancolombia.model.generatepdf.EconomicInformationPdf;
import co.com.bancolombia.model.generatepdf.FinancialInformationPdf;
import co.com.bancolombia.model.generatepdf.ForeignCurrencyPdf;
import co.com.bancolombia.model.generatepdf.InternationalOperationPdf;
import co.com.bancolombia.model.generatepdf.TributaryInformationPdf;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CIIU;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MONEDA;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PROFESSION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RESPONSE_SN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TAX_COUNTRY;

@RequiredArgsConstructor
public class GeneratePdfTaxUseCaseImpl implements GeneratePdfTaxUseCase {

    private final GeneratePdfUtilOneUseCase generatePdfUtilOneUseCase;

    @Override
    public ContactInformationPdf contactInformationPdf(ContactInfoCompReply contactResInfo) {
        return ContactInformationPdf.builder()
                .residenceAddress(contactResInfo.getAddress())
                .neighborhood(contactResInfo.getNeighborhood())
                .city(generatePdfUtilOneUseCase.getGeographicName(
                        contactResInfo.getCity(), contactResInfo.getCountry(), Numbers.ONE.getIntNumber()))
                .department(generatePdfUtilOneUseCase.getGeographicName(
                        contactResInfo.getDepartment(), contactResInfo.getCountry(), Numbers.TWO.getIntNumber()))
                .country(generatePdfUtilOneUseCase.getGeographicName(
                        contactResInfo.getCountry(), null, Numbers.THREE.getIntNumber()))
                .cellPhone(contactResInfo.getCellphone())
                .phone(contactResInfo.getPhone())
                .email(contactResInfo.getEmail())
                .build();
    }

    @Override
    public EconomicInformationPdf economicInformationPdf(EconomicInfoReply economicInfo) {
        return EconomicInformationPdf.builder()
                .profession(generatePdfUtilOneUseCase.getCatalogName(PROFESSION, economicInfo.getProfession()))
                .job(economicInfo.getOccupation())
                .economicActivity(economicInfo.getEconomicActivity())
                .codeCiiu(generatePdfUtilOneUseCase.getCatalogCode(CIIU, economicInfo.getCiiu()))
                .numberEmployees(economicInfo.getEmployeesNumber())
                .build();
    }

    @Override
    public CompanyInformationPdf companyInformationPdf(ContactInfoCompReply contactWorkInfo) {
        if (contactWorkInfo == null) {
            return CompanyInformationPdf.builder()
                    .companyNames("").companyAddress("").companyNeighborhood("").companyCity("")
                    .companyDepartment("").companyCountry("").companyPhone("").companyPhoneExt("")
                    .companyCellPhone("").companyEmail("").build();
        }

        return CompanyInformationPdf.builder()
                .companyNames(getValue(contactWorkInfo.getCompanyName()))
                .companyAddress(getValue(contactWorkInfo.getAddress()))
                .companyNeighborhood(getValue(contactWorkInfo.getNeighborhood()))
                .companyCity(generatePdfUtilOneUseCase.getGeographicName(
                        contactWorkInfo.getCity(), contactWorkInfo.getCountry(), Numbers.ONE.getIntNumber()))
                .companyDepartment(generatePdfUtilOneUseCase.getGeographicName(
                        contactWorkInfo.getDepartment(), contactWorkInfo.getCountry(), Numbers.TWO.getIntNumber()))
                .companyCountry(generatePdfUtilOneUseCase.getGeographicName(
                        contactWorkInfo.getCountry(), null, Numbers.THREE.getIntNumber()))
                .companyPhone(getValue(contactWorkInfo.getPhone()))
                .companyPhoneExt(getValue(contactWorkInfo.getExt()))
                .companyCellPhone(getValue(contactWorkInfo.getCellphone()))
                .companyEmail(getValue(contactWorkInfo.getEmail()))
                .build();
    }

    @Override
    public FinancialInformationPdf financialInformationPdf(EconomicInfoReply economicInfo) {
        return FinancialInformationPdf.builder()
                .monthlyIncome(economicInfo.getMonthlyIncome())
                .totalAssets(economicInfo.getTotalAssets())
                .monthlyOtherIncome(economicInfo.getOtherMonthlyIncome())
                .totalPassives(economicInfo.getTotalLiabilities())
                .detailOtherMonthIncome(economicInfo.getDetailOtherMonthlyIncome())
                .totalMonthExpenses(economicInfo.getTotalMonthlyExpenses())
                .annualSales(economicInfo.getAnnualSales())
                .closingSalesDate(economicInfo.getClosingDateSales())
                .build();
    }

    @Override
    public TributaryInformationPdf tributaryInformationPdf(TaxInfoReply taxInfo) {
        return TributaryInformationPdf.builder()
                .incomeDeclarant(generatePdfUtilOneUseCase.getCatalogName(RESPONSE_SN, taxInfo.getDeclaringIncome()))
                .withholdingAgent(generatePdfUtilOneUseCase.getCatalogName(RESPONSE_SN, taxInfo.getWithHoldingAgent()))
                .regimeIva(taxInfo.getVatRegime())
                .declareTaxInAnotherCountry(
                        generatePdfUtilOneUseCase.getCatalogName(RESPONSE_SN, taxInfo.getDeclareTaxInAnotherCountry()))
                .countryTax(conCountryTaxPdfList(taxInfo.getTaxCountryInfoList()))
                .originAssetComeFrom(taxInfo.getOriginAssetComeFrom())
                .originAssetComeFromCity(generatePdfUtilOneUseCase.getGeographicName(
                        taxInfo.getSourceCityResource(), taxInfo.getSourceCountryResource(),
                        Numbers.ONE.getIntNumber()))
                .originAssetComeFromCountry(generatePdfUtilOneUseCase.getGeographicName(
                        taxInfo.getSourceCountryResource(), null, Numbers.THREE.getIntNumber()))
                .build();
    }

    @Override
    public InternationalOperationPdf internationalOperationPdf(ForeignInfoReply foreignInfo) {
        return InternationalOperationPdf.builder()
                .performsForeignCurrencyOperations(generatePdfUtilOneUseCase.getCatalogName(
                        RESPONSE_SN, foreignInfo.getForeignCurrencyTransaction()))
                .foreignCurrencyTransactionType(foreignInfo.getForeignCurrencyInfo()
                        .stream().map(ForeignCurrencyInfoReply::getForeignCurrencyTransactionType)
                        .collect(Collectors.toList()))
                .whichForeignCurrencyOperation(foreignInfo.getForeignCurrencyInfo()
                        .stream().map(ForeignCurrencyInfoReply::getWhich)
                        .collect(Collectors.toList()))
                .foreignCurrencyList(conForeignInformationCurrencyList(foreignInfo.getForeignCurrencyInfo()))
                .build();
    }

    private List<CountryTaxPdf> conCountryTaxPdfList(List<TaxCountryInfoReply> taxCountries) {
        List<CountryTaxPdf> result = new ArrayList<>();
        taxCountries.stream().forEach(item -> {
            if (result.size() <= Integer.valueOf(Numbers.THREE.getNumber())) {
                result.add(CountryTaxPdf.builder()
                        .country(generatePdfUtilOneUseCase.getCatalogName(TAX_COUNTRY, item.getCountry()))
                        .idTax(item.getTaxId())
                        .build());
            }
        });
        return result;
    }

    private List<ForeignCurrencyPdf> conForeignInformationCurrencyList(List<ForeignCurrencyInfoReply> forCurrencies) {
        List<ForeignCurrencyPdf> result = new ArrayList<>();
        forCurrencies.stream().forEach(item -> {
            if (result.size() <= Integer.valueOf(Numbers.TWO.getNumber())) {
                result.add(ForeignCurrencyPdf.builder()
                        .foreignCurrencyTransactionType(item.getForeignCurrencyTransactionType())
                        .entityName(item.getNameEntity())
                        .productType(item.getProductType())
                        .productNumber(item.getProductNumber())
                        .averageMonthlyAmount(
                                item.getAverageMonthlyAmount() != null ? item.getAverageMonthlyAmount() : EMPTY)
                        .currency(generatePdfUtilOneUseCase.getCatalogName(MONEDA, item.getCurrency()))
                        .city(generatePdfUtilOneUseCase.getGeographicName(
                                item.getCity(), item.getCountry(), Numbers.ONE.getIntNumber()))
                        .country(generatePdfUtilOneUseCase.getGeographicName(
                                item.getCountry(), null, Numbers.THREE.getIntNumber()))
                        .build());
            }
        });
        return result;
    }

    private String getValue(String value) {
        if (value != null) {
            return value;
        }
        return EMPTY;
    }
}
