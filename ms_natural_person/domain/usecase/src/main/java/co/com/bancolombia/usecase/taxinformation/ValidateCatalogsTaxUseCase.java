package co.com.bancolombia.usecase.taxinformation;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.model.taxcountry.TaxCountry;
import co.com.bancolombia.taxinformation.TaxInformation;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.B_TAX_PAYMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.D_INCOME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.D_TAX_ANOTHER_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_VREGIME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RESPONSE_SN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.R_TAX_US_TAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SS_PAYMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.S_CITY_R;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.S_COUNTRY_R;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TAX_COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VREGIME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.WH_AGENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_LIST_TAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;

@RequiredArgsConstructor
public class ValidateCatalogsTaxUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final UtilCatalogs utilCatalogs;
    private final CoreFunctionString coreFunctionString;

    public void validateTaxInfoCountryCatalogs(TaxInformation taxInformation, List<TaxCountry> taxCountryList) {
        Map<String, List<CatalogQuery>> catalog = null;
        Map<String, List<GeographicQuery>> catalogGeo = null;
        List<CatalogQuery> listCatalog = retCatalog1(taxInformation);
        if (!listCatalog.isEmpty()){
            catalog = new HashMap<>();
            catalog.put(ERROR_CODE_CATALOG_SIN, listCatalog);
        }
        List<CatalogQuery> listCatalog2 = retCatalog2(taxCountryList);
        if (!listCatalog2.isEmpty()){
            if (listCatalog.isEmpty()){
                catalog = new HashMap<>();
            }
            assert catalog != null;
            catalog.put(ERROR_CODE_CATALOG_LIST_TAX, listCatalog2);
        }
        List<GeographicQuery> geographicQueries = retCatalogGeo(taxInformation);
        if (!geographicQueries.isEmpty()){
            catalogGeo = new HashMap<>();
            catalogGeo.put(ERROR_CODE_CATALOG_SIN, geographicQueries);
        }
        vinculationUpdateUseCase.validateCatalog(catalog, catalogGeo);
    }

    public List<CatalogQuery> retCatalog1(TaxInformation taxInformation){
        List<CatalogQuery> listCatalog = new ArrayList<>();
        String declaringIncome = taxInformation.getDeclaringIncome();
        if (utilCatalogs.valid(declaringIncome)){
            listCatalog.add(CatalogQuery.builder().code(declaringIncome).parents(RESPONSE_SN).field(D_INCOME).build());
        }
        String wHAgent = taxInformation.getWithHoldingAgent();
        if (utilCatalogs.valid(wHAgent)){
            listCatalog.add(CatalogQuery.builder().code(wHAgent).parents(RESPONSE_SN).field(WH_AGENT).build());
        }
        String vRegime = taxInformation.getVatRegime();
        if (utilCatalogs.valid(vRegime)){
            listCatalog.add(CatalogQuery.builder().code(vRegime).parents(VREGIME).field(F_VREGIME).build());
        }
        String rTTuT = taxInformation.getRequiredToTaxUsTax();
        if (utilCatalogs.valid(rTTuT)){
            listCatalog.add(CatalogQuery.builder().code(rTTuT).parents(RESPONSE_SN).field(R_TAX_US_TAX).build());
        }
        return retCatalog11(taxInformation, listCatalog);
    }

    public List<CatalogQuery> retCatalog11(TaxInformation taxInformation, List<CatalogQuery> listCatalog){
        String country = taxInformation.getCountry();
        if (utilCatalogs.valid(country)){
            listCatalog.add(CatalogQuery.builder().code(country).parents(TAX_COUNTRY).field(COUNTRY).build());
        }
        String bussTP = taxInformation.getBusinessTaxPayment();
        if (utilCatalogs.valid(bussTP)){
            listCatalog.add(CatalogQuery.builder().code(bussTP).parents(RESPONSE_SN).field(B_TAX_PAYMENT).build());
        }
        String socialSP = taxInformation.getSocialSecurityPayment();
        if (utilCatalogs.valid(socialSP)){
            listCatalog.add(CatalogQuery.builder().code(socialSP).parents(RESPONSE_SN).field(SS_PAYMENT).build());
        }
        String dTxInAnoCountry = taxInformation.getDeclareTaxInAnotherCountry();
        if (utilCatalogs.valid(dTxInAnoCountry)){
            listCatalog.add(CatalogQuery.builder()
                    .code(dTxInAnoCountry).parents(RESPONSE_SN).field(D_TAX_ANOTHER_COUNTRY).build());
        }
        return listCatalog;
    }

    public List<CatalogQuery> retCatalog2(List<TaxCountry> taxCountryList){
        List<CatalogQuery> list = new ArrayList<>();
        taxCountryList.forEach(ti -> {
            String country1 = ti.getCountry();
            if(utilCatalogs.valid(country1)){
                list.add(CatalogQuery.builder()
                        .code(country1).parents(TAX_COUNTRY).field(COUNTRY)
                        .nameList(coreFunctionString.integerToString(ti.getIdentifier())).build());
            }
        });
        return list;
    }

    public List<GeographicQuery> retCatalogGeo(TaxInformation taxInformation){
        List<GeographicQuery> listGeographic = new ArrayList<>();
        String sCoR = taxInformation.getSourceCountryResource();
        String sCiR = taxInformation.getSourceCityResource();
        if (utilCatalogs.valid(sCoR) && utilCatalogs.valid(sCiR)){
            listGeographic.add(GeographicQuery.builder().codeCountry(sCoR).codeCity(sCiR)
                    .nameCountry(S_COUNTRY_R).nameCity(S_CITY_R).build());
        }
        return listGeographic;
    }
}