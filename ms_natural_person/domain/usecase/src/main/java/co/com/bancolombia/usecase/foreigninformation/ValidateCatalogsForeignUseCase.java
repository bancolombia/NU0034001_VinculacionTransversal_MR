package co.com.bancolombia.usecase.foreigninformation;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.CatalogQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.GeographicQuery;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COUNTRY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CURRENCY_TRANSACTION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CURRENCY_TRANSACTION_PRODUCT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CURRENCY_TRANSACTION_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DEPARTMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CURRENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.F_CURRENCY_TRANSACTION_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RESPONSE_SN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TRANSACTION_PRODUCT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_LIST_FOREING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_CATALOG_SIN;

@RequiredArgsConstructor
public class ValidateCatalogsForeignUseCase {

    private final UtilCatalogs utilCatalogs;

    public void validateForeignInformation(ForeignInformation information) {
        List<CatalogQuery> listCatalog = new ArrayList<>();
        String code1 = information.getForeignCurrencyTransaction();
        if(utilCatalogs.valid(code1)){
            listCatalog.add(CatalogQuery.builder().code(code1)
                    .parents(RESPONSE_SN).field(CURRENCY_TRANSACTION).nameList(EMPTY).build());
        }
        utilCatalogs.callValidateCatalog(listCatalog, null, ERROR_CODE_CATALOG_SIN, ERROR_CODE_CATALOG_SIN);
    }

    public void validateForeignIInformationCurrency(List<ForeignInformationCurrency> list, String foreignInfo) {
        List<CatalogQuery> listCatalog = new ArrayList<>();
        List<GeographicQuery> listGeographic = new ArrayList<>();
        list.forEach(ficData -> {
            if (foreignInfo.equals(FOREIGN_INFORMATION)) {
                String code1 = ficData.getProductType();
                if (utilCatalogs.valid(code1)){
                    listCatalog.add(CatalogQuery.builder().code(code1).parents(CURRENCY_TRANSACTION_PRODUCT)
                            .field(TRANSACTION_PRODUCT).nameList(ficData.getForeignCurrencyTransactionType()).build());
                }
                String code2 = ficData.getCurrency();
                if (utilCatalogs.valid(code2)){
                    listCatalog.add(CatalogQuery.builder().code(code2).parents(CURRENCY).field(F_CURRENCY)
                            .nameList(ficData.getForeignCurrencyTransactionType()).build());
                }
                addGeographicCurrency(ficData, listGeographic);
            }
            String code3 = ficData.getForeignCurrencyTransactionType();
            if (utilCatalogs.valid(code3)){
                listCatalog.add(CatalogQuery.builder()
                        .code(code3).parents(CURRENCY_TRANSACTION_TYPE).field(F_CURRENCY_TRANSACTION_TYPE)
                        .nameList(ficData.getForeignCurrencyTransactionType()).build());
            }
        });
        utilCatalogs.callValidateCatalog(listCatalog, listGeographic,
                ERROR_CODE_CATALOG_LIST_FOREING, ERROR_CODE_CATALOG_LIST_FOREING);
    }

    public void addGeographicCurrency(ForeignInformationCurrency ficData, List<GeographicQuery> listGeographic){
        String country = ficData.getCountry();
        String city = ficData.getCity();
        if (utilCatalogs.valid(country) && utilCatalogs.valid(city)){
            listGeographic.add(GeographicQuery.builder()
                    .codeCountry(country).nameCountry(COUNTRY).codeDepartment(ficData.getDepartment())
                    .nameDepartment(DEPARTMENT).codeCity(city).nameCity(CITY)
                    .nameList(ficData.getForeignCurrencyTransactionType()).build());
        }
    }
}