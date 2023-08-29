package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.generatepdf.AcquisitionPdf;
import co.com.bancolombia.util.TransformFields;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
public class GeneratePdfConvertJsonImpl implements GeneratePdfConvertJson {

    private final TransformFields transformFields;

    @Override
    public JsonObject getInfo(AcquisitionPdf acqPdf) {
        String pattern = Constants.PDF_PATTERN_DATE;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        JsonObject info = new JsonObject();
        info = getInfoPart1(info, acqPdf, simpleDateFormat);
        info = getInfoPart2(info, acqPdf, simpleDateFormat);
        info = getInfoMaritalStatus(info, acqPdf);
        info = getInfoPart3(info, acqPdf);
        info = getInfoPart33(info, acqPdf);
        info = getInfoPart333(info, acqPdf);
        info = getInfoPart3333(info, acqPdf);
        info = getInfoPart4(info, acqPdf, simpleDateFormat);
        info = getInfoPart5(info, acqPdf);
        info = getInfoPart55(info, acqPdf);
        info = getInfoPart6(info, acqPdf);
        info = getInfoPart7(info, acqPdf);
        info = getInfoPart8(info, acqPdf);
        info = getInfoPart88(info, acqPdf);
        return info;
    }

    public JsonObject getInfoPart1(
            JsonObject info, AcquisitionPdf acqPdf, SimpleDateFormat simpleDateFormat) {

        info.addProperty(Constants.PDF_TYPE_ACQUISITION, "X");
        info.addProperty(Constants.PDF_COMPLETION_DATE, acqPdf.getCompletionDate() != null ?
                simpleDateFormat.format(acqPdf.getCompletionDate()) : "");
        info.addProperty(Constants.PDF_FIRST_NAME, acqPdf.getFirstName());
        info.addProperty(Constants.PDF_SECOND_NAME, acqPdf.getSecondName());
        info.addProperty(Constants.PDF_FIRST_SURNAME, acqPdf.getFirstSurname());
        info.addProperty(Constants.PDF_SECOND_SURNAME, acqPdf.getSecondSurname());

        switch (acqPdf.getDocumentType()) {
            case Constants.PDF_TYPE_DOCUMENT_CC:
                info.addProperty(Constants.PDF_TYPE_DOCUMENT_CC, "X");
                break;
            case Constants.PDF_TYPE_DOCUMENT_TI:
                info.addProperty(Constants.PDF_TYPE_DOCUMENT_TI, "X");
                break;
            case Constants.PDF_TYPE_DOCUMENT_CE:
                info.addProperty(Constants.PDF_TYPE_DOCUMENT_CE, "X");
                break;
            case Constants.PDF_TYPE_DOCUMENT_PA:
                info.addProperty(Constants.PDF_TYPE_DOCUMENT_PA, "X");
                break;
            case Constants.PDF_TYPE_DOCUMENT_CD:
                info.addProperty(Constants.PDF_TYPE_DOCUMENT_CD, "X");
                break;
            default:
        }
        return info;
    }

    public JsonObject getInfoPart2(
            JsonObject info, AcquisitionPdf acqPdf, SimpleDateFormat simpleDateFormat) {

        info.addProperty(Constants.PDF_DOCUMENT, acqPdf.getDocumentNumber());
        info.addProperty(Constants.PDF_EXPEDITION_DATE,
                acqPdf.getExpeditionDate() != null ? simpleDateFormat.format(acqPdf.getExpeditionDate()) : "");
        info.addProperty(Constants.PDF_EXPEDITION_PLACE, acqPdf.getExpeditionPlace());
        info.addProperty(Constants.PDF_BIRTH_DATE,
                acqPdf.getBirthDate() != null ? simpleDateFormat.format(acqPdf.getBirthDate()) : "");
        info.addProperty(Constants.PDF_BIRTH_PLACE, acqPdf.getBirthPlace());

        if (Constants.PDF_GENDER_F.equals(acqPdf.getGender())) {
            info.addProperty(Constants.PDF_GENDER_F, "X");
        } else {
            info.addProperty(Constants.PDF_GENDER_M, "X");
        }

        switch (acqPdf.getNationality()) {
            case Constants.PDF_COLOMBIANO:
                info.addProperty(Constants.PDF_COLOMBIANO, "X");
                break;
            case Constants.PDF_AMERICANO:
                info.addProperty(Constants.PDF_AMERICANO, "X");
                break;
            default:
                info.addProperty(Constants.PDF_NATIONALITY_DEF, "X");
                info.addProperty(Constants.PDF_NATIONALITY, transformFields.transform(acqPdf.getNationality(), null));
                break;
        }
        return info;
    }

    public JsonObject getInfoMaritalStatus(JsonObject info, AcquisitionPdf acqPdf) {
        switch (acqPdf.getMatiralStatus()) {
            case Constants.PDF_MARITAL_STATUS_SOLTERO:
                info.addProperty(Constants.PDF_MARITAL_STATUS_SOLTERO, "X");
                break;
            case Constants.PDF_MARITAL_STATUS_CASADO:
                info.addProperty(Constants.PDF_MARITAL_STATUS_CASADO, "X");
                break;
            case Constants.PDF_MARITAL_STATUS_UNION:
                info.addProperty(Constants.PDF_MARITAL_STATUS_UNION, "X");
                break;
            default:
        }
        return info;
    }

    public JsonObject getInfoPart3(JsonObject info, AcquisitionPdf acqPdf) {
        info.addProperty(Constants.PDF_RESIDENCE_ADDRESS, acqPdf.getContactInformationPdf().getResidenceAddress());
        info.addProperty(Constants.PDF_NEIGHBORHOOD, acqPdf.getContactInformationPdf().getNeighborhood());
        info.addProperty(Constants.PDF_CITY, acqPdf.getContactInformationPdf().getCity());
        info.addProperty(Constants.PDF_DEPARTMENT, acqPdf.getContactInformationPdf().getDepartment());
        info.addProperty(Constants.PDF_COUNTRY, acqPdf.getContactInformationPdf().getCountry());
        info.addProperty(Constants.PDF_PHONE, acqPdf.getContactInformationPdf().getPhone() != null ?
                acqPdf.getContactInformationPdf().getPhone() : "");
        info.addProperty(Constants.PDF_CELL_PHONE, acqPdf.getContactInformationPdf().getCellPhone());
        info.addProperty(Constants.PDF_EMAIL, acqPdf.getContactInformationPdf().getEmail());
        info.addProperty(Constants.PDF_PROFESSION, acqPdf.getEconomicInformationPdf().getProfession());
        return info;
    }

    public JsonObject getInfoPart33(JsonObject info, AcquisitionPdf acqPdf) {
        switch (acqPdf.getEconomicInformationPdf().getJob()) {
            case Constants.PDF_OCUPAC_01:
                info.addProperty(Constants.PDF_OCUPAC_01, "X");
                break;
            case Constants.PDF_OCUPAC_02:
                info.addProperty(Constants.PDF_OCUPAC_02, "X");
                break;
            case Constants.PDF_OCUPAC_03:
                info.addProperty(Constants.PDF_OCUPAC_03, "X");
                break;
            case Constants.PDF_OCUPAC_04:
                info.addProperty(Constants.PDF_OCUPAC_04, "X");
                break;
            case Constants.PDF_OCUPAC_05:
                info.addProperty(Constants.PDF_OCUPAC_05, "X");
                break;
            default:
        }
        return info;
    }

    public JsonObject getInfoPart333(JsonObject info, AcquisitionPdf acqPdf) {
        switch (acqPdf.getEconomicInformationPdf().getJob()) {
            case Constants.PDF_OCUPAC_06:
                info.addProperty(Constants.PDF_OCUPAC_06, "X");
                break;
            case Constants.PDF_OCUPAC_07:
                info.addProperty(Constants.PDF_OCUPAC_07, "X");
                break;
            case Constants.PDF_OCUPAC_08:
                info.addProperty(Constants.PDF_OCUPAC_08, "X");
                break;
            case Constants.PDF_OCUPAC_09:
                info.addProperty(Constants.PDF_OCUPAC_09, "X");
                break;
            default:

        }
        return info;
    }

    public JsonObject getInfoPart3333(JsonObject info, AcquisitionPdf acqPdf) {
        switch (acqPdf.getEconomicInformationPdf().getJob()) {
            case Constants.PDF_OCUPAC_10:
                info.addProperty(Constants.PDF_OCUPAC_10, "X");
                break;
            case Constants.PDF_OCUPAC_11:
                info.addProperty(Constants.PDF_OCUPAC_11, "X");
                break;
            case Constants.PDF_OCUPAC_12:
                info.addProperty(Constants.PDF_OCUPAC_12, "X");
                break;
            case Constants.PDF_OCUPAC_13:
                info.addProperty(Constants.PDF_OCUPAC_13, "X");
                break;
            case Constants.PDF_OCUPAC_14:
                info.addProperty(Constants.PDF_OCUPAC_14, "X");
                break;
            default:
        }
        return info;
    }

    public String transformCIUU(String code) {
        return code.replace(Constants.CIIU_CODE, "");
    }

    public JsonObject getInfoPart4(
            JsonObject info, AcquisitionPdf acqPdf, SimpleDateFormat simpleDateFormat) {

        info.addProperty(Constants.PDF_ECONOMIC_ACTIVITY, acqPdf.getEconomicInformationPdf().getEconomicActivity());
        info.addProperty(Constants.PDF_CODE_CIIU, transformCIUU(acqPdf.getEconomicInformationPdf().getCodeCiiu()));
        info.addProperty(Constants.PDF_NUMBER_EMPLOYEES, acqPdf.getEconomicInformationPdf().getNumberEmployees());
        info.addProperty(Constants.PDF_COMPANY_NAMES, acqPdf.getCompanyInformationPdf().getCompanyNames());
        info.addProperty(Constants.PDF_COMPANY_ADDRESS, acqPdf.getCompanyInformationPdf().getCompanyAddress());
        info.addProperty(Constants.PDF_COMPANY_NEIGHBORHOOD,acqPdf.getCompanyInformationPdf().getCompanyNeighborhood());
        info.addProperty(Constants.PDF_COMPANY_CITY, acqPdf.getCompanyInformationPdf().getCompanyCity());
        info.addProperty(Constants.PDF_COMPANY_DEPARTMENT, acqPdf.getCompanyInformationPdf().getCompanyDepartment());
        info.addProperty(Constants.PDF_COMPANY_COUNTRY, acqPdf.getCompanyInformationPdf().getCompanyCountry());
        info.addProperty(Constants.PDF_COMPANY_PHONE, acqPdf.getCompanyInformationPdf().getCompanyPhone());
        info.addProperty(Constants.PDF_COMPANY_PHONE_EXT, acqPdf.getCompanyInformationPdf().getCompanyPhoneExt());
        info.addProperty(Constants.PDF_COMPANY_CELLPHONE, acqPdf.getCompanyInformationPdf().getCompanyCellPhone());
        info.addProperty(Constants.PDF_COMPANY_EMAIL, acqPdf.getCompanyInformationPdf().getCompanyEmail());
        info.addProperty(Constants.PDF_MONTHLY_INCOME, acqPdf.getFinancialInformationPdf().getMonthlyIncome());
        info.addProperty(Constants.PDF_TOTAL_ASSETS, acqPdf.getFinancialInformationPdf().getTotalAssets());
        info.addProperty(Constants.PDF_MONTHLY_OTHER_INCOME,
                acqPdf.getFinancialInformationPdf().getDetailOtherMonthIncome());
        info.addProperty(Constants.PDF_TOTAL_PASSIVES, acqPdf.getFinancialInformationPdf().getTotalPassives());
        info.addProperty(Constants.PDF_DETAIL_OTHER_MONTH_INCOME,
                acqPdf.getFinancialInformationPdf().getDetailOtherMonthIncome());
        info.addProperty(Constants.PDF_TOTAL_MONTH_EXPENSES,
                acqPdf.getFinancialInformationPdf().getTotalMonthExpenses());
        info.addProperty(Constants.PDF_ANNUAL_SALES, acqPdf.getFinancialInformationPdf().getAnnualSales());
        info.addProperty(Constants.PDF_CLOSING_SALES_DATE,
                acqPdf.getFinancialInformationPdf().getClosingSalesDate() != null
                        ? simpleDateFormat.format(acqPdf.getFinancialInformationPdf().getClosingSalesDate()) : "");
        return info;
    }

    public JsonObject getInfoPart5(JsonObject info, AcquisitionPdf acqPdf) {
        if (Constants.PDF_YES.equals(acqPdf.getTributaryInformationPdf().getIncomeDeclarant())) {
            info.addProperty(Constants.PDF_INCOME_DECLARANT_YES, "X");
        } else {
            info.addProperty(Constants.PDF_INCOME_DECLARANT_NOT, "X");
        }

        if (Constants.PDF_YES.equals(acqPdf.getTributaryInformationPdf().getWithholdingAgent())) {
            info.addProperty(Constants.PDF_WITH_HOLDING_AGENT_YES, "X");
        } else {
            info.addProperty(Constants.PDF_WITH_HOLDING_AGENT_NOT, "X");
        }

        if (Constants.PDF_YES.equals(acqPdf.getTributaryInformationPdf().getDeclareTaxInAnotherCountry())) {
            info.addProperty(Constants.PDF_DECLARE_TAX_IN_ANOTHER_COUNTRY_YES, "X");
        } else {
            info.addProperty(Constants.PDF_DECLARE_TAX_IN_ANOTHER_COUNTRY_NOT, "X");
        }
        return info;
    }

    public JsonObject getInfoPart55(JsonObject info, AcquisitionPdf acqPdf) {
        switch (acqPdf.getTributaryInformationPdf().getRegimeIva()) {
            case Constants.PDF_REGIME_01:
                info.addProperty(Constants.PDF_REGIME_01, "X");
                break;
            case Constants.PDF_REGIME_02:
                info.addProperty(Constants.PDF_REGIME_02, "X");
                break;
            case Constants.PDF_REGIME_03:
                info.addProperty(Constants.PDF_REGIME_03, "X");
                break;
            default:
        }
        return info;
    }

    public JsonObject getInfoPart6(JsonObject info, AcquisitionPdf acqPdf) {
        for (int i = 0; i < acqPdf.getTributaryInformationPdf().getCountryTax().size(); i++) {
            if (i <= Constants.TWO &&
                    acqPdf.getTributaryInformationPdf().getCountryTax().get(i) != null) {
                info.addProperty(Constants.PDF_COUNTRY_TAX + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getTributaryInformationPdf().getCountryTax().get(i).getCountry());
                info.addProperty(Constants.PDF_ID_TAX + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getTributaryInformationPdf().getCountryTax().get(i).getIdTax());
            }
        }

        info.addProperty(Constants.PDF_ORIGIN_ASSET_COME_FROM,
                acqPdf.getTributaryInformationPdf().getOriginAssetComeFrom());
        info.addProperty(Constants.PDF_ORIGIN_ASSET_COME_FROM_CITY,
                acqPdf.getTributaryInformationPdf().getOriginAssetComeFromCity());
        info.addProperty(Constants.PDF_ORIGIN_ASSET_COME_FROM_COUNTRY,
                acqPdf.getTributaryInformationPdf().getOriginAssetComeFromCountry());
        if (Constants.PDF_YES.equals(acqPdf.getInternationalOperationPdf().getPerformsForeignCurrencyOperations())) {
            info.addProperty(Constants.PDF_REALIZA_OPERACIONES_MONEDA_EXTRANJERA_YES, "X");
        } else {
            info.addProperty(Constants.PDF_REALIZA_OPERACIONES_MONEDA_EXTRANJERA_NOT, "X");
        }
        return info;
    }

    public JsonObject getInfoPart7(JsonObject info, AcquisitionPdf acqPdf) {
        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().stream().forEach(item -> {
            switch (item.getForeignCurrencyTransactionType()) {
                case Constants.PDF_TIPOPE_002:
                    info.addProperty(Constants.PDF_TIPOPE_002, "X");
                    break;
                case Constants.PDF_TIPOPE_003:
                    info.addProperty(Constants.PDF_TIPOPE_003, "X");
                    break;
                case Constants.PDF_TIPOPE_004:
                    info.addProperty(Constants.PDF_TIPOPE_004, "X");
                    break;
                case Constants.PDF_TIPOPE_005:
                    info.addProperty(Constants.PDF_TIPOPE_005, "X");
                    break;
                default:
            }
        });
        return info;
    }

    public JsonObject getInfoPart77(JsonObject info, AcquisitionPdf acqPdf) {
        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().stream().forEach(item -> {
            switch (item.getForeignCurrencyTransactionType()) {
                case Constants.PDF_TIPOPE_006:
                    info.addProperty(Constants.PDF_TIPOPE_006, "X");
                    break;
                case Constants.PDF_TIPOPE_007:
                    info.addProperty(Constants.PDF_TIPOPE_007, "X");
                    break;
                case Constants.PDF_TIPOPE_008:
                    info.addProperty(Constants.PDF_TIPOPE_008, "X");
                    break;
                case Constants.PDF_TIPOPE_009:
                    info.addProperty(Constants.PDF_TIPOPE_009, "X");
                    break;
                default:
            }
        });
        return info;
    }

    public JsonObject getInfoPart8(JsonObject info, AcquisitionPdf acqPdf) {
        for (int i = 0; i < acqPdf.getInternationalOperationPdf().getForeignCurrencyList().size(); i++) {
            if (i <= 1 && acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i) != null) {
                info.addProperty(Constants.PDF_NOMBRE_ENTIDAD + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i).getEntityName());

                switch (acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i).getProductType()) {
                    case Constants.PDF_TIPOPR_01:
                        info.addProperty(Constants.PDF_TIPOPR_01 + Constants.PDF_TEXT_GUION_BAJO + i, "X");
                        break;
                    case Constants.PDF_TIPOPR_02:
                        info.addProperty(Constants.PDF_TIPOPR_02 + Constants.PDF_TEXT_GUION_BAJO + i, "X");
                        break;
                    case Constants.PDF_TIPOPR_03:
                        info.addProperty(Constants.PDF_TIPOPR_03 + Constants.PDF_TEXT_GUION_BAJO + i, "X");
                        break;
                    default:
                }
            }
        }
        return info;
    }

    public JsonObject getInfoPart88(JsonObject info, AcquisitionPdf acqPdf) {
        for (int i = 0; i < acqPdf.getInternationalOperationPdf().getForeignCurrencyList().size(); i++) {
            if (i <= 1 && acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i) != null) {
                info.addProperty(Constants.PDF_NRO_PRODUCTO + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i).getProductNumber());
                info.addProperty(Constants.PDF_MONTO_MENSUAL_PROMEDIO + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getInternationalOperationPdf().getForeignCurrencyList()
                                .get(i).getAverageMonthlyAmount());
                info.addProperty(Constants.PDF_MONEDA + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i).getCurrency());
                info.addProperty(Constants.PDF_CITY_FOREIGN + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i).getCity());
                info.addProperty(Constants.PDF_COUNTRY_FOREIGN + Constants.PDF_TEXT_GUION_BAJO + i,
                        acqPdf.getInternationalOperationPdf().getForeignCurrencyList().get(i).getCountry());
            }
        }
        return info;
    }
}
